package com.amped94.ffbtracker.data.model.db.dao

import androidx.room.*
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.LeagueAndPlayers
import com.amped94.ffbtracker.data.model.db.entity.User

@Dao
interface LeagueDao {
    @Query("SELECT * FROM League")
    suspend fun getAll(): List<League>

    @Query("SELECT * FROM League WHERE leagueId = :id")
    suspend fun getLeague(id: Long): League

    @Query("SELECT * FROM League WHERE leagueId IN (:ids)")
    suspend fun getLeagues(ids: List<Long>): List<League>

    @Query("SELECT * FROM League WHERE associatedUserId = :userId")
    suspend fun getLeaguesForUser(userId: Long): List<League>

    @Query("SELECT * FROM League WHERE associatedUserId IN (:userIds)")
    suspend fun getLeaguesForUsers(userIds: List<Long>): List<League>

    @Transaction
    @Query("SELECT * FROM League")
    suspend fun getAllLeaguesAndPlayers(): List<LeagueAndPlayers>

    @Transaction
    @Query("SELECT * FROM League WHERE leagueId = :leagueId")
    suspend fun getLeagueAndPlayers(leagueId: Long): LeagueAndPlayers

    @Query("SELECT * FROM League WHERE leagueId = (SELECT MAX(leagueId) FROM League)")
    suspend fun getLatestleague(): League?

    @Update
    suspend fun updateLeague(league: League)

    @Insert
    suspend fun insert(vararg league: League): List<Long>

    @Delete
    suspend fun delete(vararg league: League)
}