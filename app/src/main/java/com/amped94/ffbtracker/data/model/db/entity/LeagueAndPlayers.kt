package com.amped94.ffbtracker.data.model.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class LeagueAndPlayers(
    @Embedded val league: League,
    @Relation(
        parentColumn = "leagueId",
        entityColumn = "playerId",
        associateBy = Junction(PlayerLeagueCrossRef::class)
    )
    val players: List<Player>
)
