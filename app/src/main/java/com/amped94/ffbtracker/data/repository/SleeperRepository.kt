package com.amped94.ffbtracker.data.repository

import com.amped94.ffbtracker.data.api.SleeperApi
import com.amped94.ffbtracker.data.model.db.AppDatabase
import com.amped94.ffbtracker.data.model.db.FantasyProvider
import com.amped94.ffbtracker.data.model.db.entity.*

object SleeperRepository {
    val db: AppDatabase = AppDatabase.instance

    suspend fun getUserAndLeagues(username: String): UserAndLeagues {
        val userAndLeagues = db.userDao().getUserAndLeagues(username)

        return userAndLeagues ?: run {
            val userResponse = SleeperApi.getSleeperUser(username)
            val userId = db.userDao().insert(
                User(
                    userId = 0,
                    username = username,
                    type = FantasyProvider.sleeper,
                    accountId = userResponse.userId
                )
            ).first()

            val leaguesResponse = SleeperApi.getSleeperLeagues(userResponse.userId)
            val queriedPlayers = db.playerDao().getAll()

            val allPlayers = if (queriedPlayers.isNotEmpty()) queriedPlayers else {
                val playersReponse = SleeperApi.getAllPlayers()
                val roomPlayers = playersReponse.map {
                    Player(
                        playerId = 0,
                        externalPlayerId = it.key,
                        name = it.value.fullName ?: "",
                        team = ""
                    )
                }
                db.playerDao().insert(*roomPlayers.toTypedArray())
                db.playerDao().getAll()
            }

            val crossRefs = mutableListOf<PlayerLeagueCrossRef>()

            leaguesResponse.forEach { league ->
                val leagueId = db.leagueDao().insert(
                    League(
                        leagueId = 0,
                        externalLeagueId = league.leagueId,
                        associatedUserId = userId,
                        name = league.name
                    )
                ).first()

                SleeperApi.getLeagueParticipants(league).firstOrNull { participant ->
                    participant.ownerId == userResponse.userId
                }?.let { participant ->
                    participant.players.forEach { sleeperPlayerId ->
                        val playerId = allPlayers.first { roomPlayer ->
                            roomPlayer.externalPlayerId == sleeperPlayerId
                        }.playerId
                        crossRefs.add(
                            PlayerLeagueCrossRef(
                                playerId = playerId,
                                leagueId = leagueId
                            )
                        )
                    }
                }
            }

            db.playerLeagueCrossRefDao().insert(*crossRefs.toTypedArray())

            val user = db.userDao().getUserAndLeagues(username)
            return user ?: getUserAndLeagues(username)
        }
    }

    suspend fun fetchAllPlayers() {
        val players = db.playerDao().getAll()

        if (players.isEmpty()) {
            val playersResponse = SleeperApi.getAllPlayers()

            val newPlayers = playersResponse.map {
                Player(
                    playerId = 0,
                    externalPlayerId = it.key,
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