package com.amped94.ffbtracker.data.repository

import com.amped94.ffbtracker.data.api.SleeperApi
import com.amped94.ffbtracker.data.model.db.AppDatabase
import com.amped94.ffbtracker.data.model.db.entity.FantasyProvider
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.model.db.entity.User

object SleeperRepository {
    val db: AppDatabase = AppDatabase.instance

    suspend fun getUser(username: String): User {
        val user = db.userDao().getUser(username)

        return user ?: run {
            val userResponse = SleeperApi.getSleeperUser(username)
            val leaguesResponse = SleeperApi.getSleeperLeagues(userResponse.userId)
            val leagues = mutableListOf<League>()

            leaguesResponse.forEach { league ->
                SleeperApi.getLeagueParticipants(league).firstOrNull { participant ->
                    participant.ownerId == userResponse.userId
                }?.let { participant ->
                    leagues.add(
                        League(
                            id = 0,
                            leagueId = league.leagueId,
                            players = participant.players
                        )
                    )
                }
            }

            db.leagueDao().insert(*leagues.toTypedArray())
            val newLeagues = db.leagueDao().getAll()

            val newUser = User(
                id = 0,
                username = userResponse.username,
                type = FantasyProvider.sleeper,
                accountId = userResponse.userId,
                leagues = newLeagues.map { it.id }
            )

            db.userDao().insert(newUser)

            newUser
        }
    }

    suspend fun getLeagues(user: User): List<League> {
        return db.leagueDao().getLeagues(user.leagues)
    }

    suspend fun fetchAllPlayers() {
        val players = db.playerDao().getAll()

        if (players.isEmpty()) {
            val playersResponse = SleeperApi.getAllPlayers()

            val newPlayers = playersResponse.map {
                Player(
                    id = 0,
                    playerId = it.key,
                    name = it.value.fullName ?: "",
                    team = ""
                )
            }

            db.playerDao().insert(*newPlayers.toTypedArray())
        }
    }

    suspend fun getPlayersById(ids: List<String>): List<Player> {
        if (db.playerDao().getAll().isEmpty()) {
            fetchAllPlayers()
        }

        val players = db.playerDao().getPlayers(ids)

        return players
    }
}