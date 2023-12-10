package com.project.lembretio

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

interface EventCreator {
    var name: String
    var times: MutableList<String>
    var date: String
    var repeating: Boolean
    var alarmId: Int
    var eventId: Int
    var uri: Uri?
    var isMedication: Boolean

    fun addEvent(event: Event)
}

class EventPagerActivity : AppCompatActivity(), EventCreator{
    private val eventViewModel: EventViewModel by viewModels {
        EventModelFactory((application as EventApplication).repository)
    }

    override var name: String = ""
    override var times: MutableList<String> = mutableListOf()
    override var date: String = ""
    override var repeating: Boolean = false
    override var alarmId: Int = 0
    override var eventId: Int = -1
    override var uri: Uri? = null
    override var isMedication: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_pager)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager)

        isMedication = intent.getBooleanExtra("is_med", false)

        val adapter = if (isMedication) MedicationPagerAdapter(this) else AppointmentPagerAdapter(this)
        viewPager2.isUserInputEnabled = false
        viewPager2.adapter = adapter

        val initialTitle = intent.getStringExtra("title")
        if (initialTitle != null) {
            name = initialTitle
        }

        eventId = intent.getIntExtra("event_id", -1)

        repeating = intent.getBooleanExtra("repeating", true)

        val initialDate = intent.getStringExtra("date")
        if (initialDate != null) {
            date = initialDate
        }

        val timeArray = intent.getStringExtra("times")
        if (timeArray != null) {
            times = timeArray.split(" ").toMutableList()
        }

        alarmId = intent.getIntExtra("alarm_id", 0)

        uri = intent.getParcelableExtra("uri")
    }

    override fun addEvent(event: Event) {
        eventViewModel.addEvent(event)
    }
}