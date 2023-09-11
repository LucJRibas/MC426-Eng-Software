package com.project.lembretio

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


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

    fun addEvent(event: Event) {
        events.add(event)
        notifyItemInserted(events.size - 1)
    }

    fun changeEventTitle(idx: Int, newTitle: String) {
        events[idx].name = newTitle
        notifyItemChanged(idx)

    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.itemView.apply {
            val button = findViewById<TextView>(R.id.btnEvent)
            button.text = event.name
            button.setOnClickListener {
                val intent = Intent(it.context, EventActivity::class.java)

                intent.putExtra("title", event.name)
                intent.putExtra("idx", position)

                startActivity(it.context, intent, null)            }
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}