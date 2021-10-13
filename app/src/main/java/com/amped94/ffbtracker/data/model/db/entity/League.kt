package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class League(
    @PrimaryKey(autoGenerate = true) val leagueId: Long,
    val externalLeagueId: String,
    val associatedUserId: Long,
    val name: String
)
