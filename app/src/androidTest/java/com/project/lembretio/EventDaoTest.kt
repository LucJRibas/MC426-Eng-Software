package com.project.lembretio

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
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
    fun insertAndRetrieveEvent() {
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
    fun updateEvent() {
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
    fun deleteEvent() {
        val event = Event(name = "Test Event", repeating = false)

        eventDao.insert(event)
        eventDao.delete(event)

        val retrievedEvent = eventDao.getEventById(1) // Assuming the first event has an id of 1

        assert(retrievedEvent == null)
    }

    @Test
    fun testGetAllEvents() {
        val event1 = Event(name = "Event 1", repeating = false)
        val event2 = Event(name = "Event 2", repeating = true)

        // Inserir eventos no banco de dados
        eventDao.insert(event1)
        eventDao.insert(event2)

        // Obter todos os eventos do banco de dados
        val allEvents = eventDao.getAllEvents()

        assertEquals(2, allEvents.size) // Verifica se há dois eventos
        assertEquals(event1.name, allEvents[0].name)
        assertEquals(event2.name, allEvents[1].name)
    }

    @Test
    fun testGetEventById() {
        val event1 = Event(name = "Event 1", repeating = false)
        val event2 = Event(name = "Event 2", repeating = true)
        val event3 = Event(name = "Event 3", repeating = false)

        // Inserir os eventos no banco de dados
        eventDao.insert(event1)
        eventDao.insert(event2)
        eventDao.insert(event3)

        // Obter os eventos pelo ID
        val retrievedEvent1 = eventDao.getEventById(1) // Supondo que o primeiro evento tenha um ID de 1
        val retrievedEvent2 = eventDao.getEventById(2)
        val retrievedEvent3 = eventDao.getEventById(3)

        assertNotNull(retrievedEvent1)
        assertNotNull(retrievedEvent2)
        assertNotNull(retrievedEvent3)

        assertEquals(event1.name, retrievedEvent1?.name)
        assertEquals(event2.name, retrievedEvent2?.name)
        assertEquals(event3.name, retrievedEvent3?.name)
    }
}
