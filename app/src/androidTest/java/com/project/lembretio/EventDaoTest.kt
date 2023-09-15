package com.project.lembretio

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EventDaoTest {
    private lateinit var eventDao: EventDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build().also { db = it }
        eventDao = db.EventDao()
    }


    @Throws(IOException::class)
    @After
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndRetrieveEvent() = runBlocking {
        createDb()
        val event = Event(name = "Test Event", repeating = false)

        eventDao.insert(event)

        val retrievedEvent = eventDao.getEventById(1) // Assuming the first event has an id of 1

        assert(retrievedEvent != null)
        assert(retrievedEvent?.name == event.name)
        assert(retrievedEvent?.repeating == event.repeating)
        assert(retrievedEvent?.isChecked == event.isChecked)
    }

    @Test
    @Throws(Exception::class)
    fun updateEvent() = runBlocking {
        val event = Event(name = "Test Event", repeating = false)

        eventDao.insert(event)

        val updatedEvent = event.copy(name = "Updated Event")
        eventDao.update(updatedEvent)

        val retrievedEvent = eventDao.getEventById(1) // Assuming the first event has an id of 1

        assert(retrievedEvent != null)
        assert(retrievedEvent?.name == updatedEvent.name)
    }

    @Test
    @Throws(Exception::class)
    fun deleteEvent() = runBlocking {
        val event = Event(name = "Test Event", repeating = false)

        eventDao.insert(event)
        eventDao.delete(event)

        val retrievedEvent = eventDao.getEventById(1) // Assuming the first event has an id of 1

        assert(retrievedEvent == null)
    }
}
