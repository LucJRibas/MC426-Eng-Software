package com.project.lembretio

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "event_table")
@Parcelize
data class Event(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "repeating") var repeating: Boolean,
    @ColumnInfo(name = "date") var date: LocalDate,
    @ColumnInfo(name = "times") var times: MutableList<LocalTime>,
    @ColumnInfo(name = "alarm_id") var alarmId: Int,
    @ColumnInfo(name = "uri") var uri: Uri?,
    @ColumnInfo(name = "is_med") var isMedication: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable {

    val createdDateFormatted : String
        get() =date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    fun setDateFromString(stringDate: String) {
        date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

}