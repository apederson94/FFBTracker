package com.amped94.ffbtracker.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SleeperLeagueResponse(
    @SerialName("league_id") val leagueId: String,
    val name: String,
    @SerialName("roster_positions") val rosterPositions: List<String>
)
