package com.project.lembretio

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val adapter = EventAdapter()

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
    fun cancelButtonTest(){
        EventApplication.adapter = adapter
        adapter.addEvent(Event("Test", false))
        launchActivity<EventActivity>().use {
            onView(withId(R.id.btnCancel)).perform(click())
        }
        assertEquals(1, adapter.itemCount)
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