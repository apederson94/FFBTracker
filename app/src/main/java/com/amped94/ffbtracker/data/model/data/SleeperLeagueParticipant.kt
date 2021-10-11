package com.amped94.ffbtracker.data.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SleeperLeagueParticipant(
    val players: List<String>,
    @SerialName("owner_id") val ownerId: String?
)
