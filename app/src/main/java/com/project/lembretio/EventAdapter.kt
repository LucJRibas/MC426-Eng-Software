package com.project.lembretio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable


class EventAdapter(
    private val events: MutableList<Event> = mutableListOf()
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_event,
                parent,
                false
            )
        )
    }

    fun addEvent(todo: Event) {
        events.add(todo)
        notifyItemInserted(events.size - 1)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.itemView.apply {
            val title = findViewById<TextView>(R.id.tvEventTitle)
            title.text = event.name
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}