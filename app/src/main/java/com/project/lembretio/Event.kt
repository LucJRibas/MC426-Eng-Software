package com.project.lembretio

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class Event(

    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "repeating") val repeating: Boolean,
    @ColumnInfo(name = "isChecked") var isChecked: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0 // Defina um valor padrão de 0 para que o Room saiba que é auto-gerado
)