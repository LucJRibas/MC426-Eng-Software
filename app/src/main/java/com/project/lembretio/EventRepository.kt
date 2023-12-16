package com.project.lembretio

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao)
{
    val allEvents: Flow<List<Event>> = eventDao.getAllEvents()
    @WorkerThread
    suspend fun insertEvent(event: Event)
    {
        eventDao.insert(event)
    }

    @WorkerThread
    suspend fun updateEvent(event: Event)
    {
        eventDao.update(event)
    }

    @WorkerThread
    suspend fun deleteEvent(event: Event)
    {
        eventDao.delete(event)
    }
}