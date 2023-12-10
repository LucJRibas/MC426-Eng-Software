package com.project.lembretio

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "event_table")
@Parcelize
data class Event(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "repeating") var repeating: Boolean,
    @ColumnInfo(name = "date") var date: LocalDateTime,
    @ColumnInfo(name = "alarm_id") var alarmId: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {

    val createdDateFormatted : String
        get() =date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

}