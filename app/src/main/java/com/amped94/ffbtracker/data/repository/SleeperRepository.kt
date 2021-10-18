package com.amped94.ffbtracker.data.repository

import androidx.preference.PreferenceManager
import com.amped94.ffbtracker.MainApplication
import com.amped94.ffbtracker.data.api.SleeperApi
import com.amped94.ffbtracker.data.api.model.SleeperLeagueParticipant
import com.amped94.ffbtracker.data.model.db.AppDatabase
import com.amped94.ffbtracker.data.model.db.FantasyProvider
import com.amped94.ffbtracker.data.model.db.entity.*
import com.amped94.ffbtracker.data.model.viewModel.Position
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*

object SleeperRepository {
    val db: AppDatabase = AppDatabase.instance

    suspend fun getPlayersAndLeaguesInitial(): List<PlayerAndLeagues> {
        val sleeperUsers = db.userDao().getUsersWithType(FantasyProvider.Sleeper)
        val sleeperLeagues = db.leagueDao().getLeaguesForUsers(sleeperUsers.map { it.userId })
        val playersForLeagues =
            db.playerLeagueCrossRefDao().getEntriesForLeagues(sleeperLeagues.map { it.leagueId })

        db.userDao().delete(*sleeperUsers.toTypedArray())
        db.leagueDao().delete(*sleeperLeagues.toTypedArray())
        db.playerLeagueCrossRefDao().delete(*playersForLeagues.toTypedArray())

        return getPlayersAndLeagues()
    }

    suspend fun getPlayersAndLeagues(): List<PlayerAndLeagues> {
        val queriedResults = db.playerDao().getPlayersAndLeagues()
        val sleeperLeaguesLoaded = queriedResults.any {
            it.leagues.any { league ->
                league.type == FantasyProvider.Sleeper
            }
        }

        return if (sleeperLeaguesLoaded) queriedResults else {
            val queriedUsers = db.userDao().getAll()
            val prefs = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext())

            val users = if (queriedUsers.isNotEmpty()) queriedUsers else {
                val username = prefs.getString("sleeperUsername", "")
                if (username.isNullOrEmpty()) return emptyList() else listOf(getUser(username))
            }

            users.forEach { user ->
                val leaguesForUser = getLeaguesForUser(user)
                getPlayersInLeagues(leaguesForUser)
            }

            db.playerDao().getPlayersAndLeagues()
        }
    }

    suspend fun getPlayersInLeagues(leagues: List<League>): List<PlayerAndLeagues> {
        val leagueIds = leagues.map { it.leagueId }
        val queriedPlayersInLeagues = db.playerDao().getPlayersInLeagues(leagueIds)

        return if (queriedPlayersInLeagues.isNotEmpty()) queriedPlayersInLeagues else {
            val allPlayers = getAllPlayers()
            val participantApiCalls = mutableListOf<LeagueAndParticipantCall>()
            val playerAndLeagues = mutableListOf<PlayerAndLeagues>()

            coroutineScope {
                leagues.forEach { league ->
                    val user = db.userDao().getUser(league.associatedUserId)
                    val sleeperLeagueParticipant = async {
                        Pair(league, SleeperApi.getLeagueParticipants(league).firstOrNull {
                            it.ownerId == user?.accountId
                        })
                    }
                    participantApiCalls.add(sleeperLeagueParticipant)
                }
                try {
                    val allParticipants = participantApiCalls.map { it.await() }
                    allParticipants.map {
                        val crossRefs = it.second?.let { participant ->
                            val playersInLeague = participant.players.map { playerId ->
                                allPlayers.first { roomPlayer ->
                                    roomPlayer.externalPlayerId == playerId
                                }
                            }
                            playersInLeague.map { player ->
                                PlayerLeagueCrossRef(
                                    playerId = player.playerId,
                                    it.first.leagueId
                                )
                            }
                        }

                        crossRefs?.let { allRefs ->
                            db.playerLeagueCrossRefDao().insert(*allRefs.toTypedArray())
                            playerAndLeagues.addAll(db.playerDao().getPlayersInLeagues(leagueIds))
                        }
                    }
                } catch (exception: Exception) {
                    print(exception)
                }
                playerAndLeagues
            }
        }
    }

    suspend fun getLeaguesForUser(user: User): List<League> {
        val querieidLeagues = db.leagueDao().getLeaguesForUser(user.userId)

        return if (querieidLeagues.isNotEmpty()) querieidLeagues else {
            val leaguesResponse = SleeperApi.getSleeperLeagues(user.accountId)
            val leaguesToInsert = mutableListOf<League>()

            leaguesResponse.forEach { league ->

                leaguesToInsert.add(
                    League(
                        leagueId = 0,
                        externalLeagueId = league.leagueId,
                        associatedUserId = user.userId,
                        name = league.name,
                        type = FantasyProvider.Sleeper
                    )
                )
            }

            val leagueIds = db.leagueDao().insert(*leaguesToInsert.toTypedArray())

            db.leagueDao().getLeagues(leagueIds)
        }
    }

    suspend fun getUser(username: String): User {
        val user = db.userDao().getUser(username)

        return user ?: run {
            val userResponse = SleeperApi.getSleeperUser(username)
            val userId = db.userDao().insert(
                User(
                    userId = 0,
                    username = userResponse.username,
                    type = FantasyProvider.Sleeper,
                    accountId = userResponse.userId
                )
            ).first()

            db.userDao().getUser(userId) ?: getUser(username)
        }
    }

    suspend fun getAllPlayers(): List<Player> {
        val queriedPlayers = db.playerDao().getAll()

        val prefs = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext())
        val timestamp = prefs.getLong("previousPlayerQueryTimestamp", 0)
        val timestampDate = Calendar.getInstance().apply {
            timeInMillis = timestamp
            add(Calendar.HOUR, 24)
        }

        return if (queriedPlayers.isNotEmpty() && timestampDate.after(Calendar.getInstance())) queriedPlayers else {
            val playersReponse = SleeperApi.getAllPlayers()
            val roomPlayers = playersReponse.map {
                Player(
                    playerId = 0,
                    externalPlayerId = it.key,
                    firstName = it.value.firstName,
                    lastName = it.value.lastName,
                    age = it.value.age ?: 0,
                    number = it.value.number ?: 0,
                    team = it.value.team ?: "FA",
                    position = it.value.position ?: ""
                )
            }
            db.playerDao().insert(*roomPlayers.toTypedArray())
            db.playerDao().getAll()
        }
    }

    suspend fun getLeaguesAndPlayers(): List<LeagueAndPlayers> {
        val leaguesAndPlayers = db.leagueDao().getAllLeaguesAndPlayers()

        return leaguesAndPlayers
    }

    suspend fun saveLeague(league: League) {
        db.leagueDao().insert(league)
    }

    suspend fun saveLeagueAndPlayers(league: League, players: List<Player>) {
        val newLeagueId = db.leagueDao().insert(league).first()
        val crossRefs = players.map {
            PlayerLeagueCrossRef(
                playerId = it.playerId,
                leagueId = newLeagueId
            )
        }
        db.playerLeagueCrossRefDao().insert(*crossRefs.toTypedArray())
    }

    suspend fun getLatestLeague(): League {
        return db.leagueDao().getLatestleague()!!
    }

    suspend fun getLeague(leagueId: Long): League {
        return db.leagueDao().getLeague(leagueId)
    }

    suspend fun savePlayersToLeague(crossRefs: List<PlayerLeagueCrossRef>) {
        db.playerLeagueCrossRefDao().insert(*crossRefs.toTypedArray())
    }

    suspend fun searchPlayersByPositionAndName(
        position: Position,
        searchText: String
    ): List<Player> {
        return when (position) {
            Position.DST -> db.playerDao().searchPlayersByPosition("DEF", searchText)
            else -> db.playerDao().searchPlayersByPosition(position.title, searchText)
        }
    }

    suspend fun updateLeague(league: League) {
        db.leagueDao().updateLeague(league)
    }

    suspend fun updatePlayerLeagueCrossRefs(
        leagueId: Long,
        newCrossRefs: List<PlayerLeagueCrossRef>
    ) {
        val currentCrossRefs = db.playerLeagueCrossRefDao().getEntriesForLeagues(listOf(leagueId))
        db.playerLeagueCrossRefDao().delete(*currentCrossRefs.toTypedArray())
        db.playerLeagueCrossRefDao().insert(*newCrossRefs.toTypedArray())
    }

    suspend fun getLeagueAndPlayers(leagueId: Long): LeagueAndPlayers {
        return db.leagueDao().getLeagueAndPlayers(leagueId)
    }

    suspend fun deleteLeague(league: League) {
        db.leagueDao().delete(league)
        db.playerLeagueCrossRefDao().cleanUpEntries()
    }
}

typealias LeagueAndParticipantCall = Deferred<Pair<League, SleeperLeagueParticipant?>>