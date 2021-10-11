package com.amped94.ffbtracker.data.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SleeperLeague(
    @SerialName("league_id") val leagueId: String,
    val name: String
)
