package com.project.lembretio

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
//    private val eventActivity = EventActivity()
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.project.lembretio", appContext.packageName)
    }

    @Test
    fun addEventTest(){
        adapter.addEvent(Event("Test", false))
        assertEquals(adapter.itemCount, 1)
    }

    @Test
    fun changeEventTitleTest(){
        adapter.addEvent(Event("Test", false))
        adapter.changeEventTitle(0, "New name")
        assertEquals(adapter.itemCount, 1)
        assertEquals(adapter[0].name, "New name")
    }

//    @Test
//    fun cancelButtonTest(){
//        adapter.addEvent(Event("Test", false))
//        launchActivity<EventActivity>().use { scenario ->
//            scenario.onActivity { activity ->
//                onView(withId(R.id.btnCancel)).perform(click())
//
//                val resultCode = scenario.result.resultCode
//
//            }
//
//        }
////        eventActivity.cancelButton.callOnClick()
//        assertEquals(adapter.itemCount, 1)
//    }
//    @Test
//    fun submitButtonTest(){
//
//    }
}