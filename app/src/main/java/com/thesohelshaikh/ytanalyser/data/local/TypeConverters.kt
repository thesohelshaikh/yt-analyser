package com.thesohelshaikh.ytanalyser.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object RoomTypeConverters {
    @TypeConverter
    fun fromString(value: String): List<String?>? {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromArrayList(list: List<String?>?): String {
        return Json.encodeToString(list)
    }
}