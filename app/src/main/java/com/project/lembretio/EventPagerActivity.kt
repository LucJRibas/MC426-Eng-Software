package com.project.lembretio

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

interface EventCreator {
    var name: String
    var times: MutableList<String>
    var date: String
    var repeating: Boolean

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_pager)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager)
        val adapter = EventPagerAdapter(this)
        viewPager2.isUserInputEnabled = false
        viewPager2.adapter = adapter
    }

    override fun addEvent(event: Event) {
        eventViewModel.addEvent(event)
//        Toast.makeText(applicationContext, "Medição adicionada!", Toast.LENGTH_SHORT).show()
    }
}