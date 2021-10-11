package com.amped94.ffbtracker.data.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SleeperUser(
    val username: String = "",
    @SerialName("user_id") val userId: String = "",
    @SerialName("display_name") val displayName: String = "",
    val avatar: String = ""
)