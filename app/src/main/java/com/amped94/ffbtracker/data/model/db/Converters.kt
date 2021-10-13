package com.amped94.ffbtracker.data.model.db

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LeagueConverter {

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

    @TypeConverter
    fun fromFantasyProvider(value: FantasyProvider): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toFantasyProvider(value: Int): FantasyProvider {
        return FantasyProvider.values()[value]
    }
}