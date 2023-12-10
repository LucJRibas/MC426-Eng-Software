package com.project.lembretio.addeventfragments

import android.app.Activity
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
import com.project.lembretio.Event
import com.project.lembretio.EventCreator
import com.project.lembretio.MainActivity
import com.project.lembretio.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
            val alarmId = Math.toIntExact(LocalDateTime.now().getLong(ChronoField.EPOCH_DAY))
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            builder.addEvent(Event(
                builder.name,
                builder.repeating,
                LocalDate.parse(builder.date, formatter),
                builder.times.map { LocalTime.parse(it) }.toMutableList(),
                alarmId,
                uri
            ))
            val intentBack = Intent(context, MainActivity::class.java)
            startActivity(intentBack)
        }
        prevButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }
}