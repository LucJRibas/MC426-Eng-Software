package com.project.lembretio

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class Event(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "repeating") val repeating: Boolean,
    @ColumnInfo(name = "date") var date: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)