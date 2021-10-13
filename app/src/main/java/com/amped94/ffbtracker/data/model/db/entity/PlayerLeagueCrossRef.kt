package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["playerId", "leagueId"])
data class PlayerLeagueCrossRef(
    val playerId: Long,
    val leagueId: Long
)
