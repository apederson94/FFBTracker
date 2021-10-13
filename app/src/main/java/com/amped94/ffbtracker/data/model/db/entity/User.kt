package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amped94.ffbtracker.data.model.db.FantasyProvider

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Long,
    val username: String,
    val type: FantasyProvider,
    val accountId: String
)