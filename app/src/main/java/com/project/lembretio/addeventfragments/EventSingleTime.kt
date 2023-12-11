package com.project.lembretio.addeventfragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.project.lembretio.EventCreator
import com.project.lembretio.MainActivity
import com.project.lembretio.R
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class EventSingleTime : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var timeButton: Button
    private lateinit var timeText: TextView
    private lateinit var titleText: TextView
    private lateinit var eventCreator: EventCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventCreator = context as EventCreator
        val layout = inflater.inflate(R.layout.fragment_event_single_time, container, false)
        nextButton = layout.findViewById(R.id.btn_title_next)
        prevButton = layout.findViewById(R.id.btn_title_prev)
        timeButton = layout.findViewById(R.id.btn_time_pick)
        titleText = layout.findViewById(R.id.single_time_title_text)
        timeText = layout.findViewById(R.id.tv_single_time)

        val viewPager: ViewPager2? = activity?.findViewById(R.id.view_pager)

        eventCreator.event.repeating = false
        if (eventCreator.event.times.isEmpty()) {
            eventCreator.event.times.add(LocalTime.of(0, 0))
        }
        timeText.text = eventCreator.event.times[0].toString()

        timeButton.setOnClickListener {
            val cal : Calendar = Calendar.getInstance()
            val setTimeListener = TimePickerDialog.OnTimeSetListener{
                    _, pickedHour, pickedMinute ->
                val hour = String.format("%02d", pickedHour);
                val minute = String.format("%02d", pickedMinute)
                eventCreator.event.times[0] = LocalTime.of(pickedHour, pickedMinute)
                timeText.text = "$hour:$minute"
            }
            val timePickerDialog = TimePickerDialog(context,setTimeListener,cal.get(Calendar.HOUR_OF_DAY), cal.get(
                Calendar.MINUTE),true)
            timePickerDialog.setOnCancelListener {
                Toast.makeText(
                    context,
                    "A data foi não pode ser definida.",
                    Toast.LENGTH_SHORT
                ).show()   //shows the toast if input field is empty
            }
            timePickerDialog.setCancelable(false)
            timePickerDialog.show()
        }


        nextButton.setOnClickListener {
            if (!eventCreator.event.isMedication) {
                val dateTime = LocalDateTime.of(eventCreator.event.date, eventCreator.event.times[0])
                if (dateTime.isBefore(LocalDateTime.now())) {
                    Toast.makeText(context, "A consulta deve ser em uma data/horário futuros", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
        }
        prevButton.setOnClickListener {
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }
}