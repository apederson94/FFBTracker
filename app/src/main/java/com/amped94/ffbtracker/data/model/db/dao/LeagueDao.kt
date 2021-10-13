package com.amped94.ffbtracker.data.model.db.dao

import androidx.room.*
import com.amped94.ffbtracker.data.model.db.entity.League

@Dao
interface LeagueDao {
    @Query("SELECT * FROM League")
    suspend fun getAll(): List<League>

    @Query("SELECT * FROM League WHERE leagueId = :id")
    suspend fun getLeague(id: Int): League

    @Query("SELECT * FROM League WHERE leagueId IN (:ids)")
    suspend fun getLeagues(ids: List<Int>): List<League>

    @Update
    suspend fun updateLeague(league: League)

    @Insert
    suspend fun insert(vararg league: League): List<Long>

    @Delete
    suspend fun delete(league: League)
}