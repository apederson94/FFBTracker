package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true) val playerId: Long,
    val externalPlayerId: String,
    val name: String,
    val team: String
)
