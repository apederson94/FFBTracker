package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndLeagues(
    @Embedded val user: User,
    @Relation(
        entity = League::class,
        parentColumn = "userId",
        entityColumn = "associatedUserId"
    )
    val leagues: List<LeagueAndPlayers>
)
