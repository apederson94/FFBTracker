package com.amped94.ffbtracker.data.model.db

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LeagueConverter {
//    @TypeConverter
//    fun fromInt(value: Int): FantasyProvider {
//        return FantasyProvider.values()[value]
//    }
//
//    @TypeConverter
//    fun fantasyProviderToInt(provider: FantasyProvider): Int {
//        return provider.ordinal
//    }

    @TypeConverter
    fun toStringList(jsonString: String): List<String> {
        return Json.decodeFromString(jsonString)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIntList(jsonString: String): List<Int> {
        return Json.decodeFromString(jsonString)
    }

    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return Json.encodeToString(value)
    }
}