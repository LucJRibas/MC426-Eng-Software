package com.project.lembretio

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.lembretio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val eventViewModel: EventViewModel by viewModels {
        EventModelFactory((application as EventApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventViewModel.events.observe(this){
            binding.rvEventActivity.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                binding.rvEventActivity.adapter = EventAdapter(it.toMutableList())
            }
        }

//        binding.rvEventActivity.layoutManager = LinearLayoutManager(applicationContext)
//        if (EventApplication.adapter  == null) {
//            eventViewModel.addEvent(Event("event 1", false))
//            eventViewModel.addEvent(Event("event 2", false))
//            EventApplication.adapter = eventViewModel.events.value?.let { EventAdapter(it.toMutableList()) }
//        }
//
//        binding.rvEventActivity.adapter = EventApplication.adapter

        binding.btnCreateEvent.setOnClickListener {
            val intent = Intent(applicationContext, EventActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }
}