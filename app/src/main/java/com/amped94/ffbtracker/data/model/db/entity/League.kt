package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class League(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val leagueId: String,
    val userId: Int,
    val players: List<String>
)
