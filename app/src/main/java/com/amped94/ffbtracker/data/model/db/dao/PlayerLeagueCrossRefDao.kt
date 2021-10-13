package com.amped94.ffbtracker.data.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amped94.ffbtracker.data.model.db.entity.PlayerLeagueCrossRef

@Dao
interface PlayerLeagueCrossRefDao {
    @Query("SELECT * FROM PlayerLeagueCrossRef")
    suspend fun getAll(): List<PlayerLeagueCrossRef>

    @Insert
    suspend fun insert(vararg crossRef: PlayerLeagueCrossRef): List<Long>
}