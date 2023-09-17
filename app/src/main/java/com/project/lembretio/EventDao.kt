package com.project.lembretio

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM event_table ORDER BY id ASC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("DELETE FROM event_table")
    fun deleteAll()

//    @Query("SELECT * FROM event_table WHERE id = :eventId")
//    fun getEventById(eventId: Long): Event?
}