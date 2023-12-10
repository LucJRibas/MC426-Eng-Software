package com.project.lembretio.utils

import android.net.Uri
import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
class Converters {
    @TypeConverter
    fun toDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it)}
    }

    @TypeConverter
    fun fromDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @TypeConverter
    fun fromAppUsageDataList(value : MutableList<LocalTime>) = Json.encodeToString(value)

    @OptIn(ExperimentalSerializationApi::class)
    @TypeConverter
    fun toAppUsageDataList(value: String) = Json.decodeFromString<MutableList<LocalTime>>(value)


    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(value: String?): Uri? {
        return value?.let { Uri.parse(it)}
    }
}