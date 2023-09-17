package com.project.lembretio

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class Event(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "repeating") val repeating: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

    /*val uid: Int = 0 // Defina um valor padrão de 0 para que o Room saiba que é auto-gerado

     */
)