package com.project.lembretio

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
            val button = findViewById<LinearLayout>(R.id.llEventInfo)
            val titleText = findViewById<TextView>(R.id.textTitle)
            val dateText = findViewById<TextView>(R.id.textDate)
            titleText.text = event.name
            dateText.text = event.createdDateFormatted
            button.setOnClickListener {
                val intent = Intent(it.context, EventActivity::class.java)

                intent.putExtra("title", event.name)
                intent.putExtra("event_id", event.id)
                intent.putExtra("date", event.createdDateFormatted)
                intent.putExtra("alarm_id", event.alarmId)

                startActivity(it.context, intent, null)
            }

            val removeButton = findViewById<TextView>(R.id.btnRemove)
            removeButton.setOnClickListener {
                val alarmIntent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    event.alarmId,
                    alarmIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                pendingIntent?.let { _pendingIntent->
                    alarmManager.cancel(_pendingIntent)
                }
                eventViewModel.deleteEvent(event)
            }
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }
}