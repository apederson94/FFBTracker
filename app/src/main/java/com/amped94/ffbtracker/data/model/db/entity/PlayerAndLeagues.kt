package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlayerAndLeagues(
    @Embedded val player: Player,
    @Relation(
        parentColumn = "playerId",
        entityColumn = "leagueId",
        associateBy = Junction(PlayerLeagueCrossRef::class)
    )
    val leagues: List<League>
)
