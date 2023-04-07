package com.thesohelshaikh.ytanalyser.data.local

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.reflect.Type


object RoomTypeConverters {
    @TypeConverter
    fun fromString(value: String): List<String?>? {
        val listType: Type = object : TypeToken<List<String?>?>() {}.type
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromArrayList(list: List<String?>?): String {
        return Json.encodeToString(list)
    }
}