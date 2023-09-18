package com.project.lembretio

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
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
        eventDao = db.eventDao()
    }


    @Throws(IOException::class)
    @After
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndRetrieveEvent() {
        val event = Event(name = "Test Event", repeating = false)

        runBlocking {
            eventDao.insert(event)
            val id = eventDao.getAllEvents().first().first().id
            val retrievedEvent = eventDao.getEventById(id)
            assert(retrievedEvent != null)
            assert(retrievedEvent?.name == event.name)
            assert(retrievedEvent?.repeating == event.repeating)
        }

    }

    @Test
    @Throws(Exception::class)
    fun updateEvent() = runBlocking {
        val event = Event(name = "Test Event", repeating = false)
        eventDao.insert(event)

        val id = eventDao.getAllEvents().first().first().id

        val updatedEvent = event.copy(name = "Updated Event", id = id)

        eventDao.update(updatedEvent)

        val retrievedEvent = eventDao.getEventById(id)
        assert(retrievedEvent != null) { "Event should not be null" }
        assert(retrievedEvent?.name == updatedEvent.name) { "Event should have been updated (name: ${retrievedEvent?.name} expected: ${updatedEvent.name}"}
    }


    @Test
    @Throws(Exception::class)
    fun deleteEvent() = runBlocking {

        val event = Event(name = "Test Event", repeating = false)
        eventDao.insert(event)
        val insertedEventId = event.id

        eventDao.delete(event.copy(id = insertedEventId))

        val retrievedEvent = eventDao.getEventById(insertedEventId)
        assert(retrievedEvent == null) { "Event should have been deleted" }
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetAllEvents() {
        val event1 = Event(name = "Event 1", repeating = false)
        val event2 = Event(name = "Event 2", repeating = true)

        val allEvents = mutableListOf<Event>()

        // Inserir eventos no banco de dados
        runBlocking {
            val testDispatcher = StandardTestDispatcher()

            eventDao.insert(event1)
            eventDao.insert(event2)

            allEvents.addAll(eventDao.getAllEvents().first())
        }

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
        runBlocking {
            eventDao.insert(event1)
            eventDao.insert(event2)
            eventDao.insert(event3)
        }

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
