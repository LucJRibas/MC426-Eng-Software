import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.project.lembretio.AppDatabase
import com.project.lembretio.Event
import com.project.lembretio.EventDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime

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
        val event = Event(
            name = "Test Event",
            repeating = false,
            date = LocalDate.now(),
            time = LocalTime.now(),
            times = mutableListOf(LocalTime.now().plusMinutes(1)),
            alarmId = 1,
            uri = null,
            isMedication = true,
            comment = "Test comment"
        )

        runBlocking {
            eventDao.insert(event)
            val id = eventDao.getAllEvents().first().first().id
            val retrievedEvent = eventDao.getEventById(id)
            assertNotNull(retrievedEvent)
            assertEquals(event.name, retrievedEvent?.name)
            assertEquals(event.repeating, retrievedEvent?.repeating)
            assertEquals(event.date, retrievedEvent?.date)
            assertEquals(event.time, retrievedEvent?.time)
            assertEquals(event.times, retrievedEvent?.times)
            assertEquals(event.alarmId, retrievedEvent?.alarmId)
            assertEquals(event.uri, retrievedEvent?.uri)
            assertEquals(event.isMedication, retrievedEvent?.isMedication)
            assertEquals(event.comment, retrievedEvent?.comment)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateEvent() = runBlocking {
        val event = Event(
            name = "Test Event",
            repeating = false,
            date = LocalDate.now(),
            time = LocalTime.now(),
            times = mutableListOf(LocalTime.now().plusMinutes(1)),
            alarmId = 1,
            uri = null,
            isMedication = true,
            comment = "Test comment"
        )
        eventDao.insert(event)

        val id = eventDao.getAllEvents().first().first().id

        val updatedEvent = event.copy(name = "Updated Event", id = id)

        eventDao.update(updatedEvent)

        val retrievedEvent = eventDao.getEventById(id)
        assertNotNull(retrievedEvent)
        assertEquals(updatedEvent.name, retrievedEvent?.name)
    }

    @Test
    @Throws(Exception::class)
    fun deleteEvent() = runBlocking {
        val event = Event(
            name = "Test Event",
            repeating = false,
            date = LocalDate.now(),
            time = LocalTime.now(),
            times = mutableListOf(LocalTime.now().plusMinutes(1)),
            alarmId = 1,
            uri = null,
            isMedication = true,
            comment = "Test comment"
        )
        eventDao.insert(event)
        val insertedEventId = event.id

        eventDao.delete(event.copy(id = insertedEventId))

        val retrievedEvent = eventDao.getEventById(insertedEventId)
        assertNull(retrievedEvent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetAllEvents() {
        val event1 = Event(
            name = "Event 1",
            repeating = false,
            date = LocalDate.now(),
            time = LocalTime.now(),
            times = mutableListOf(LocalTime.now().plusMinutes(1)),
            alarmId = 1,
            uri = null,
            isMedication = true,
            comment = "Test comment"
        )
        val event2 = Event(
            name = "Event 2",
            repeating = true,
            date = LocalDate.now(),
            time = LocalTime.now(),
            times = mutableListOf(LocalTime.now().plusMinutes(1)),
            alarmId = 2,
            uri = null,
            isMedication = false,
            comment = ""
        )

        val allEvents = mutableListOf<Event>()

        runBlocking {
            val testDispatcher = StandardTestDispatcher()

            eventDao.insert(event1)
            eventDao.insert(event2)

            allEvents.addAll(eventDao.getAllEvents().first())
        }

        assertEquals(2, allEvents.size)
        assertEquals(event1.name, allEvents[0].name)
        assertEquals(event2.name, allEvents[1].name)
    }

    @Test
    fun testGetEventById() {
        val event1 = Event(
            name = "Event 1",
            repeating = false,
            date = LocalDate.now(),
            time = LocalTime.now(),
            times = mutableListOf(LocalTime.now().plusMinutes(1)),
            alarmId = 1,
            uri = null,
            isMedication = true,
            comment = "Test comment"
        )
        val event2 = Event(
            name = "Event 2",
            repeating = true,
            date = LocalDate.now(),
            time = LocalTime.now(),
            times = mutableListOf(LocalTime.now().plusMinutes(1)),
            alarmId = 2,
            uri = null,
            isMedication = false,
            comment = ""
        )
        val event3 = Event(
            name = "Event 3",
            repeating = false,
            date = LocalDate.now(),
            time = LocalTime.now(),
            times = mutableListOf(LocalTime.now().plusMinutes(1)),
            alarmId = 3,
            uri = null,
            isMedication = true,
            comment = "Another comment"
        )

        runBlocking {
            eventDao.insert(event1)
            eventDao.insert(event2)
            eventDao.insert(event3)
        }

        val retrievedEvent1 = eventDao.getEventById(event1.id)
        val retrievedEvent2 = eventDao.getEventById(event2.id)
        val retrievedEvent3 = eventDao.getEventById(event3.id)

        assertNotNull(retrievedEvent1)
        assertNotNull(retrievedEvent2)
        assertNotNull(retrievedEvent3)

        assertEquals(event1.name, retrievedEvent1?.name)
        assertEquals(event2.name, retrievedEvent2?.name)
        assertEquals(event3.name, retrievedEvent3?.name)
    }
}
