package com.amped94.ffbtracker.data.repository

import androidx.preference.PreferenceManager
import com.amped94.ffbtracker.MainApplication
import com.amped94.ffbtracker.data.api.SleeperApi
import com.amped94.ffbtracker.data.model.db.AppDatabase
import com.amped94.ffbtracker.data.model.db.FantasyProvider
import com.amped94.ffbtracker.data.model.db.entity.*
import java.util.*

object SleeperRepository {
    val db: AppDatabase = AppDatabase.instance

    suspend fun getPlayersAndLeaguesInitial(): List<PlayerAndLeagues> {
        val sleeperUsers = db.userDao().getUsersWithType(FantasyProvider.Sleeper)
        val sleeperLeagues = db.leagueDao().getLeaguesForUsers(sleeperUsers.map { it.userId })
        val playersForLeagues = db.playerLeagueCrossRefDao().getEntriesForLeagues(sleeperLeagues.map { it.leagueId })
        db.playerLeagueCrossRefDao().delete(*playersForLeagues.toTypedArray())

        return getPlayersAndLeagues()
    }

    suspend fun getPlayersAndLeagues(): List<PlayerAndLeagues> {
        val queriedResults = db.playerDao().getPlayersAndLeagues()

        return if (queriedResults.isNotEmpty()) queriedResults else {
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
            val crossRefs = mutableListOf<PlayerLeagueCrossRef>()

            leagues.forEach { league ->
                SleeperApi.getLeagueParticipants(league)
                    .firstOrNull { participant ->
                        val user = db.userDao().getUser(league.associatedUserId)
                        participant.ownerId == user?.accountId
                    }?.let { participant ->
                        participant.players.forEach { sleeperPlayerId ->
                            val playerId = allPlayers.first { roomPlayer ->
                                roomPlayer.externalPlayerId == sleeperPlayerId
                            }.playerId
                            crossRefs.add(
                                PlayerLeagueCrossRef(
                                    playerId = playerId,
                                    leagueId = league.leagueId
                                )
                            )
                        }
                    }
            }

            db.playerLeagueCrossRefDao().insert(*crossRefs.toTypedArray())

            db.playerDao().getPlayersInLeagues(leagueIds)
        }
    }

    suspend fun getLeaguesForUser(user: User): List<League> {
        val querieidLeagues = db.leagueDao().getLeaguesForUser(user.userId)

        return if (querieidLeagues.isNotEmpty()) querieidLeagues else {
            val leaguesResponse = SleeperApi.getSleeperLeagues(user.accountId)
            val leaguesToInsert = mutableListOf<League>()

            leaguesResponse.forEach { league ->
                var numQB = 0
                var numRB = 0
                var numWR = 0
                var numTE = 0
                var numFLEX = 0
                var numK = 0
                var numDST = 0
                var numSuperFLEX = 0
                var numBench = 0

                league.rosterPositions.forEach {
                    when (it) {
                        "QB" -> numQB++
                        "RB" -> numRB++
                        "WR" -> numWR++
                        "TE" -> numTE++
                        "FLEX" -> numFLEX++
                        "K" -> numK++
                        "DST" -> numDST++
                        "SUPER_FLEX" -> numSuperFLEX++
                        "BN" -> numBench++
                    }
                }

                leaguesToInsert.add(
                    League(
                        leagueId = 0,
                        externalLeagueId = league.leagueId,
                        associatedUserId = user.userId,
                        name = league.name,
                        type = FantasyProvider.Sleeper,
                        numQB = numQB,
                        numRB = numRB,
                        numWR = numWR,
                        numTE = numTE,
                        numFLEX = numFLEX,
                        numK = numK,
                        numDST = numDST,
                        numSuperFLEX = numSuperFLEX,
                        numBench = numBench
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
                    team = it.value.team ?: "NO TEAM",
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
}