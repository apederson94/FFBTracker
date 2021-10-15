package com.amped94.ffbtracker.data.model.db.dao

import androidx.room.*
import com.amped94.ffbtracker.data.api.model.SleeperPlayer
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.model.db.entity.PlayerAndLeagues

@Dao
interface PlayerDao {
    @Query("SELECT * FROM Player")
    suspend fun getAll(): List<Player>

    @Query("SELECT * FROM Player WHERE playerId = :id")
    suspend fun getPlayer(id: Int): Player?

    @Query("SELECT * FROM Player WHERE playerId IN (:ids)")
    suspend fun getPlayers(ids: List<String>): List<Player>

    @Transaction
    @Query("SELECT * FROM Player WHERE playerId IN (SELECT playerId FROM PlayerLeagueCrossRef)")
    suspend fun getPlayersAndLeagues(): List<PlayerAndLeagues>

    @Transaction
    @Query("SELECT * FROM Player WHERE playerId IN (SELECT playerId FROM PlayerLeagueCrossRef WHERE leagueId IN (:leagueIds))")
    suspend fun getPlayersInLeagues(leagueIds: List<Long>): List<PlayerAndLeagues>

    @Insert
    suspend fun insert(vararg player: Player): List<Long>

    @Delete
    suspend fun delete(vararg player: Player)
}