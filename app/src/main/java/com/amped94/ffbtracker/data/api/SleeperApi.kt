package com.amped94.ffbtracker.data.api

import com.amped94.ffbtracker.data.api.model.SleeperLeagueParticipant
import com.amped94.ffbtracker.data.api.model.SleeperLeagueResponse
import com.amped94.ffbtracker.data.api.model.SleeperPlayerResponse
import com.amped94.ffbtracker.data.api.model.SleeperUserResponse
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import java.util.*

object SleeperApi {

    private fun getClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    }
                )
            }
        }
    }

    suspend fun getSleeperUser(username: String): SleeperUserResponse {
        return getClient().use {
            it.get("https://api.sleeper.app/v1/user/$username")
        }
    }

    suspend fun getSleeperLeagues(userId: String): List<SleeperLeagueResponse> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return getClient().use {
            it.get("https://api.sleeper.app/v1/user/$userId/leagues/nfl/$year")
        }
    }

    suspend fun getLeagueParticipants(league: SleeperLeagueResponse): List<SleeperLeagueParticipant> {
        return getClient().use {
            it.get("https://api.sleeper.app/v1/league/${league.leagueId}/rosters")
        }
    }

    suspend fun getAllPlayers(): SleeperPlayerResponse {
        return getClient().use {
            it.get("https://api.sleeper.app/v1/players/nfl")
        }
    }
}