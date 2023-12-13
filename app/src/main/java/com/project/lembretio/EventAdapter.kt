package com.project.lembretio

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class EventAdapter(
    private val events: List<Event>,
    private val eventViewModel: EventViewModel,
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
            val layout = findViewById<LinearLayout>(R.id.llEventInfo)
            val titleText = findViewById<TextView>(R.id.textTitle)
            val dateText = findViewById<TextView>(R.id.textDate)
            titleText.text = event.name

            if (!event.isMedication) {
                layout.setBackgroundColor(Color.parseColor("#a3507b"))
            }
            if (event.repeating) {
                dateText.text = "Todos os dias: ${event.times.joinToString(separator = ", ") { it.toString() }}"
            } else {
                dateText.text = "${event.createdDateFormatted}: ${event.times.joinToString(separator = ", ") { it.toString() }}"
            }
            layout.setOnClickListener {
                val intent = Intent(it.context, TimelineActivity::class.java)

                intent.putExtra("event", event)

                startActivity(it.context, intent, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}