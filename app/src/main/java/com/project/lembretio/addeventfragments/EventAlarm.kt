package com.project.lembretio.addeventfragments

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.project.lembretio.AlarmReceiver
import com.project.lembretio.Event
import com.project.lembretio.EventCreator
import com.project.lembretio.MainActivity
import com.project.lembretio.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField


class EventAlarm : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var alarmButton: Button
    private lateinit var alarmText: TextView
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_event_alarm, container, false)
        nextButton = layout.findViewById(R.id.btn_title_next)
        prevButton = layout.findViewById(R.id.btn_title_prev)
        alarmButton = layout.findViewById(R.id.btn_alarm)
        alarmText = layout.findViewById(R.id.tv_alarm)

        val viewPager: ViewPager2? = activity?.findViewById(R.id.view_pager)

        if ((context as EventCreator).uri != null) {
            uri = (context as EventCreator).uri
            val ringtone = RingtoneManager.getRingtone(context, uri)
            alarmText.text = ringtone.getTitle(context)
        }


        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val ringtoneIntent: Intent? = result.data
                uri = ringtoneIntent?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                val ringtone = RingtoneManager.getRingtone(context, uri)
                alarmText.text = ringtone.getTitle(context)
            }
        }
        alarmButton.setOnClickListener {
            val ringtoneIntent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Selecione um toque de alarme")
            ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
            resultLauncher.launch(ringtoneIntent)


        }

        nextButton.setOnClickListener {
            val builder = (context as EventCreator)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val newEvent = Event(
                builder.name,
                builder.repeating,
                LocalDate.parse(builder.date, formatter),
                builder.times.map { LocalTime.parse(it) }.toMutableList(),
                if (builder.alarmId == 0) Math.toIntExact(LocalDateTime.now().getLong(ChronoField.EPOCH_DAY)) else builder.alarmId,
                uri,
                if (builder.eventId == -1) 0 else builder.eventId
            )
            scheduleAlarmForEvent(newEvent)
            builder.addEvent(newEvent)

            val intentBack = Intent(context, MainActivity::class.java)
            startActivity(intentBack)
        }
        prevButton.setOnClickListener {
            (context as EventCreator).uri = uri
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }

    private fun scheduleAlarmForEvent(event: Event){

        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val eventTimes = event.times.map { LocalDateTime.of(event.date, it) }

        eventTimes.forEachIndexed { i, dateTime ->
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            alarmIntent.putExtra("title", event.name)
            alarmIntent.putExtra("event_id", event.id)
            alarmIntent.putExtra("date", dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            alarmIntent.putExtra("alarm_id", event.alarmId + i)
            alarmIntent.putExtra("repeating", event.repeating)
            alarmIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, uri)

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dateTime.atZone(ZoneId.of("America/Sao_Paulo")).toInstant().toEpochMilli(),
                PendingIntent.getBroadcast(
                    context,
                    event.alarmId + i,
                    alarmIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }
    }
}