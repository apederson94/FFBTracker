package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val username: String,
    val type: FantasyProvider,
    val accountId: String,
    val leagues: List<Int>
)

enum class FantasyProvider {
    sleeper, other
}