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
    fun toDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it)}

    @TypeConverter
    fun fromDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun fromAppUsageDataList(value : MutableList<LocalTime>) = value.joinToString(separator = " ") { it.toString() }

    @TypeConverter
    fun toAppUsageDataList(value: String) = value.split(" ").map { LocalTime.parse(it) }

    @TypeConverter
    fun fromUri(uri: Uri?): String? = uri?.toString()

    @TypeConverter
    fun toUri(value: String?): Uri? = value?.let { Uri.parse(it)}

}