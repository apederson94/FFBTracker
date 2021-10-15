package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amped94.ffbtracker.data.model.db.FantasyProvider

@Entity
data class League(
    @PrimaryKey(autoGenerate = true) val leagueId: Long,
    val externalLeagueId: String,
    val associatedUserId: Long,
    val name: String,
    val type: FantasyProvider
)
