package com.amped94.ffbtracker.data.model.db.dao

import androidx.room.*
import com.amped94.ffbtracker.data.model.db.entity.League

@Dao
interface LeagueDao {
    @Query("SELECT * FROM League")
    suspend fun getAll(): List<League>

    @Query("SELECT * FROM League WHERE id = :id")
    suspend fun getLeague(id: Int): League

    @Query("SELECT * FROM League WHERE userId = :userId")
    suspend fun getLeagues(userId: Int): List<League>

    @Update
    suspend fun updateLeague(league: League)

    @Insert
    suspend fun insert(vararg league: League)

    @Delete
    suspend fun delete(league: League)
}