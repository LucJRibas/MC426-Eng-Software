package com.project.lembretio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class EventPagerActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_pager)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager)
        val adapter = EventPagerAdapter(this)
        viewPager2.adapter = adapter
    }
}