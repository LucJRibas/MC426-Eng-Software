package com.project.lembretio

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.time.LocalDate

interface EventCreator {
    var event: Event

    fun addEvent(event: Event)
}

class EventPagerActivity : AppCompatActivity(), EventCreator{
    private val eventViewModel: EventViewModel by viewModels {
        EventModelFactory((application as EventApplication).repository)
    }

    override var event: Event = Event("", false, LocalDate.MAX, mutableListOf(), 0, null, false, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_pager)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager)

        val selectedEvent = intent.getParcelableExtra<Event>("event")
        if (selectedEvent != null) {
            event = selectedEvent
        } else {
            event.isMedication = intent.getBooleanExtra("is_med", true)
        }

        val adapter = if (event.isMedication) MedicationPagerAdapter(this) else AppointmentPagerAdapter(this)
        viewPager2.isUserInputEnabled = false
        viewPager2.adapter = adapter



    }

    override fun addEvent(event: Event) {
        eventViewModel.addEvent(event)
    }
}