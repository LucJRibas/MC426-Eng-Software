package com.project.lembretio

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

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

    lateinit var instrumentationContext: Context

    @get:Rule
    public val mRuntimePermissionRule: GrantPermissionRule =
        if (Build.VERSION.SDK_INT >= 33) {
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.project.lembretio", appContext.packageName)
    }

    @Test
    fun cancelButtonTest(){
        runBlocking {
            val event = Event(name = "Test Event", repeating = false)
            eventDao.insert(event)

            launchActivity<EventActivity>().use {
                onView(withId(R.id.btnCancel)).perform(click())
            }

            assertEquals("should not add item if canceled",1, eventDao.getAllEvents().first().size)
        }
    }

//    @Test
//    fun notifyButtonTest() {
//        launchActivity<EventActivity>().use {
//            onView(withId(R.id.btnNotify)).perform(click())
//
//            val manager: NotificationManager =
//                instrumentationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            await.until { manager.activeNotifications.isNotEmpty() }
//
//            with(manager.activeNotifications.first()) {
//                assertEquals(id, this.id)
//                assertEquals("Lembretio", this.notification.extras[Notification.EXTRA_TITLE])
//            }
//        }
//    }

    @Test
    fun submitButtonEmptyEventTitleTest(){
        runBlocking {
            val event = Event(name = "Test Event", repeating = false)
            eventDao.insert(event)

            launchActivity<EventActivity>().use {
                onView(withId(R.id.btnSubmit)).perform(click())
            }

            assertEquals("submit should not add any events if edit text is empty", 1, eventDao.getAllEvents().first().size)
        }
    }

//    @Test
//    fun submitButtonAddTest(){
//        runBlocking {
//            val event = Event(name = "Test Event", repeating = false)
//            eventDao.insert(event)
//
//            launchActivity<EventActivity>().use {
//                it.onActivity { activity ->
//                    val title = activity.findViewById<EditText>(R.id.etEventTitle)
//                    title.setText("New Event")
//                }
//                onView(withId(R.id.btnSubmit)).perform(click())
//            }
//
//            val list = eventDao.getAllEvents().first()
//            assertEquals("submit should add event if edit text is not empty",2, list.size)
//            assertEquals("New Event", list[list.size - 1].name)
//        }
//    }
//
//    @Test
//    fun submitButtonEditTest(){
//
//        runBlocking {
//            val event = Event(name = "Test Event", repeating = false)
//            eventDao.insert(event)
//
//            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//            val intent = Intent(appContext, EventActivity::class.java)
//            intent.putExtra("title", event.name)
//            intent.putExtra("idx", 0)
//            launchActivity<EventActivity>(intent).use {
//                it.onActivity { activity ->
//                    val title = activity.findViewById<EditText>(R.id.etEventTitle)
//                    title.setText("New name")
//                }
//                onView(withId(R.id.btnSubmit)).perform(click())
//            }
//
//            val list = eventDao.getAllEvents().first()
//            assertEquals("submit should not add any events if edit text is not empty",1, list.size)
//            assertEquals("New name", list.first().name)
//        }
//
//    }
}