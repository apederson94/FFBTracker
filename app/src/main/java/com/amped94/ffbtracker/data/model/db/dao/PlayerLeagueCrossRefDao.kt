package com.amped94.ffbtracker.data.model.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.amped94.ffbtracker.data.model.db.entity.PlayerLeagueCrossRef

@Dao
interface PlayerLeagueCrossRefDao {
    @Query("SELECT * FROM PlayerLeagueCrossRef")
    suspend fun getAll(): List<PlayerLeagueCrossRef>

    @Query("SELECT * FROM PlayerLeagueCrossRef WHERE leagueId in (:leagueIds)")
    suspend fun getEntriesForLeagues(leagueIds: List<Long>): List<PlayerLeagueCrossRef>

    @Insert
    suspend fun insert(vararg crossRef: PlayerLeagueCrossRef): List<Long>

    @Delete
    suspend fun delete(vararg crossRef: PlayerLeagueCrossRef)
}