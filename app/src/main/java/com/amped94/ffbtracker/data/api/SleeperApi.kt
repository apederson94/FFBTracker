package com.amped94.ffbtracker.data.api

import android.util.Log
import com.amped94.ffbtracker.data.model.data.SleeperLeague
import com.amped94.ffbtracker.data.model.data.SleeperLeagueParticipant
import com.amped94.ffbtracker.data.model.data.SleeperUser
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import java.util.*

object SleeperApi {

    private fun getClient(): HttpClient {
        return HttpClient {
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

    suspend fun getSleeperUser(username: String): SleeperUser {
        return getClient().use {
            it.get("https://api.sleeper.app/v1/user/$username")
        }
    }

    suspend fun getSleeperLeagues(userId: String): List<SleeperLeague> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return getClient().use {
            it.get("https://api.sleeper.app/v1/user/$userId/leagues/nfl/$year")
        }
    }

    suspend fun getLeagueParticipants(league: SleeperLeague): List<SleeperLeagueParticipant> {
        return getClient().use {
            it.get("https://api.sleeper.app/v1/league/${league.leagueId}/rosters")
        }
    }
}