package com.project.lembretio

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule;

import org.awaitility.Awaitility.*
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val adapter = EventAdapter()
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
    fun addEventTest(){
        adapter.addEvent(Event("Test", false))
        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun changeEventTitleTest(){
        adapter.addEvent(Event("Test", false))
        adapter.changeEventTitle(0, "New name")
        assertEquals(adapter.itemCount, 1)
        assertEquals("New name", adapter[0].name)
    }

    @Test
    fun changeEventDateTest(){
        adapter.addEvent(Event("Test", false))
        adapter.changeEventDate(0, "16-9-2023 21:05")
        assertEquals(adapter.itemCount, 1)
        assertEquals("16-9-2023 21:05"  , adapter[0].date)
    }

    @Test
    fun cancelButtonTest(){
        EventApplication.adapter = adapter
        adapter.addEvent(Event("Test", false))
        launchActivity<EventActivity>().use {
            onView(withId(R.id.btnCancel)).perform(click())
        }
        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun notifyButtonTest(){
        EventApplication.adapter = adapter
        adapter.addEvent(Event("Test", false))
        launchActivity<EventActivity>().use {
            onView(withId(R.id.btnNotify)).perform(click())

            val manager: NotificationManager = instrumentationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            await().until { manager.activeNotifications.isNotEmpty() }

            with(manager.activeNotifications.first()) {
                assertEquals(id, this.id)
                assertEquals("Lembretio", this.notification.extras[Notification.EXTRA_TITLE])
            }
        }
    }

    @Test
    fun submitButtonEmptyEventTitleTest(){
        EventApplication.adapter = adapter
        adapter.addEvent(Event("Test", false))
        launchActivity<EventActivity>().use {
            onView(withId(R.id.btnSubmit)).perform(click())
        }
        // submit should not add any events if edit text is empty
        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun submitButtonAddTest(){
        EventApplication.adapter = adapter
        adapter.addEvent(Event("Test", false))
        launchActivity<EventActivity>().use {
            it.onActivity { activity ->
                val title = activity.findViewById<EditText>(R.id.etEventTitle)
                title.setText("New Event")
            }
            onView(withId(R.id.btnSubmit)).perform(click())
        }
        // submit should add any events if edit text is not empty
        assertEquals("New Event", adapter[adapter.itemCount - 1].name)
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun submitButtonEditTest(){
        EventApplication.adapter = adapter
        adapter.addEvent(Event("Test", false))
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(appContext, EventActivity::class.java)
        intent.putExtra("title", adapter[0].name)
        intent.putExtra("idx", 0)
        launchActivity<EventActivity>(intent).use {
            it.onActivity { activity ->
                val title = activity.findViewById<EditText>(R.id.etEventTitle)
                title.setText("New name")
            }
            onView(withId(R.id.btnSubmit)).perform(click())
        }
        assertEquals(1, adapter.itemCount)
        assertEquals("New name", adapter[0].name)
    }
}