package com.project.lembretio

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class EventAdapter(
    private val events: List<Event>,
    private val eventViewModel: EventViewModel
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    operator fun get(i: Int): Event { return events[i] }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_event,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.itemView.apply {
            val button = findViewById<TextView>(R.id.btnEvent)
            button.text = event.name
            button.setOnClickListener {
                val intent = Intent(it.context, EventActivity::class.java)

                intent.putExtra("title", event.name)
                intent.putExtra("idx", event.id)

                startActivity(it.context, intent, null)
            }

            val removeButton = findViewById<TextView>(R.id.btnRemove)
            removeButton.setOnClickListener {
                eventViewModel.deleteEvent(event)
            }
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}