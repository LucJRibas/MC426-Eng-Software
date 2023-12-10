package com.project.lembretio.addeventfragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.project.lembretio.Event
import com.project.lembretio.EventApplication
import com.project.lembretio.EventCreator
import com.project.lembretio.EventModelFactory
import com.project.lembretio.EventViewModel
import com.project.lembretio.MainActivity
import com.project.lembretio.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.Calendar

class EventAlarm : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var alarmButton: Button

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

        val viewPager: ViewPager2? = activity?.findViewById(R.id.view_pager)


        alarmButton.setOnClickListener {

        }

        nextButton.setOnClickListener {
            val builder = (context as EventCreator)
            val alarmId = Math.toIntExact(LocalDateTime.now().getLong(ChronoField.EPOCH_DAY))
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            builder.addEvent(Event(builder.name, builder.repeating, LocalDateTime.parse("${builder.date} ${builder.times[0]}", formatter), alarmId))
            val intentBack = Intent(context, MainActivity::class.java)
            startActivity(intentBack)
        }
        prevButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }
}