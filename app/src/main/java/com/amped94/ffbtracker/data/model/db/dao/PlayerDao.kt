package com.amped94.ffbtracker.data.model.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.amped94.ffbtracker.data.model.db.entity.Player

@Dao
interface PlayerDao {
    @Query("SELECT * FROM Player")
    suspend fun getAll(): List<Player>

    @Query("SELECT * FROM Player WHERE id = :id")
    suspend fun getPlayer(id: Int): Player?

    @Query("SELECT * FROM Player WHERE playerId IN (:ids)")
    suspend fun getPlayers(ids: List<String>): List<Player>

    @Insert
    suspend fun insert(vararg player: Player)

    @Delete
    suspend fun delete(player: Player)
}