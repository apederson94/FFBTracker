package com.amped94.ffbtracker.data.api.model

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

typealias SleeperPlayerResponse = HashMap<String, SleeperPlayer>

@Serializable
data class SleeperPlayer (
    @SerialName("pandascore_id")
    val pandascoreID: JsonObject? = null,

    @SerialName("yahoo_id")
    val yahooID: Long? = null,

    @SerialName("swish_id")
    val swishID: Long? = null,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("rotoworld_id")
    val rotoworldID: Long? = null,

    @SerialName("fantasy_data_id")
    val fantasyDataID: Long? = null,

    val weight: String? = null,
    val number: Long? = null,

    @SerialName("rotowire_id")
    val rotowireID: Long? = null,

    @SerialName("injury_start_date")
    val injuryStartDate: JsonObject? = null,

    @SerialName("first_name")
    val firstName: String,
    val age: Long? = null,

    @SerialName("birth_date")
    val birthDate: String? = null,

    @SerialName("gsis_id")
    val gsisID: String? = null,

    @SerialName("birth_country")
    val birthCountry: JsonObject? = null,

    @SerialName("search_first_name")
    val searchFirstName: String? = null,

    @SerialName("injury_notes")
    val injuryNotes: String? = null,

    @SerialName("stats_id")
    val statsID: Long? = null,

    val college: String? = null,

    @SerialName("years_exp")
    val yearsExp: Long? = null,

    val hashtag: String? = null,

    @SerialName("birth_state")
    val birthState: JsonObject? = null,

    @SerialName("full_name")
    val fullName: String? = null,

    @SerialName("high_school")
    val highSchool: String? = null,

    @SerialName("birth_city")
    val birthCity: JsonObject? = null,

    @SerialName("news_updated")
    val newsUpdated: Long? = null,

    @SerialName("practice_description")
    val practiceDescription: String? = null,

    @SerialName("depth_chart_order")
    val depthChartOrder: Long? = null,

    val active: Boolean,

    @SerialName("sportradar_id")
    val sportradarID: String? = null,

    @SerialName("search_last_name")
    val searchLastName: String? = null,

    @SerialName("search_rank")
    val searchRank: Long? = null,

    @SerialName("espn_id")
    val espnID: Long? = null,

    @SerialName("search_full_name")
    val searchFullName: String? = null,


    @SerialName("player_id")
    val playerID: String
)
