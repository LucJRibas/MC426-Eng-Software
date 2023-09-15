package com.project.lembretio

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDao {
    @Insert
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM event_table")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM event_table WHERE id = :eventId")
    suspend fun getEventById(eventId: Long): Event?
}