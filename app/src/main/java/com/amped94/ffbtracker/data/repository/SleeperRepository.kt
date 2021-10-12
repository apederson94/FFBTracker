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
            val newUser = User(
                id = 0,
                username = userResponse.username,
                type = FantasyProvider.sleeper,
                accountId = userResponse.userId,
                leagues = emptyList()
            )

            db.userDao().insert(newUser)

            newUser
        }
    }

    suspend fun getLeagues(user: User): List<League> {
        val leagues = db.leagueDao().getLeagues(user.id)

        return if (leagues.isNotEmpty()) leagues else {
            val leaguesResponse = SleeperApi.getSleeperLeagues(user.accountId)
            val newLeagues = leaguesResponse.map {
                League(
                    id = 0,
                    leagueId = it.leagueId,
                    userId = user.id,
                    players = emptyList()
                )
            }

            db.leagueDao().insert(*newLeagues.toTypedArray())

            newLeagues
        }
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
        val players = db.playerDao().getPlayers(ids)

        return players
    }
}