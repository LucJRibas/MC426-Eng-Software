package com.project.lembretio.addeventfragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.view.indices
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.project.lembretio.AlarmReceiver
import com.project.lembretio.EventCreator
import com.project.lembretio.MainActivity
import com.project.lembretio.R
import java.time.LocalTime
import java.util.Calendar


class EventMany : Fragment() {

    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var spinner: Spinner
    private lateinit var recycler: RecyclerView
    private lateinit var eventCreator: EventCreator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventCreator = context as EventCreator
        val layout = inflater.inflate(R.layout.fragment_event_many, container, false)
        nextButton = layout.findViewById(R.id.btn_many_next)
        prevButton = layout.findViewById(R.id.btn_many_prev)
        spinner = layout.findViewById(R.id.spinner_many)
        recycler = layout.findViewById(R.id.recycler_many)
        recycler.layoutManager = LinearLayoutManager(context)

        if (eventCreator.event.times.isEmpty()) eventCreator.event.times.add(LocalTime.of(0, 0))
        val adapter = ManyAdapter(eventCreator.event.times.map { it.toString() }.toMutableList())

        recycler.adapter = adapter
        populateSpinner()
        spinner.setSelection(adapter.itemCount - 1)

        val viewPager: ViewPager2? = activity?.findViewById(R.id.view_pager)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                adapter.makeSize(spinner.selectedItem.toString().toInt())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        nextButton.setOnClickListener {
            eventCreator.event.times = adapter.times.map { LocalTime.parse(it) }.toMutableList()
            viewPager?.currentItem = viewPager?.currentItem?.plus(1)!!
        }
        prevButton.setOnClickListener {
            eventCreator.event.times = adapter.times.map { LocalTime.parse(it) }.toMutableList()
            viewPager?.currentItem = viewPager?.currentItem?.minus(1)!!
        }
        return layout
    }

    private fun populateSpinner(){
        val items: MutableList<Int> = MutableList(10) { index -> 1 + index}
        val adapter: ArrayAdapter<Int>? =
            context?.let { ArrayAdapter<Int>(it, android.R.layout.simple_spinner_item, items) }
        spinner.adapter = adapter
    }

    private inner class ManyAdapter (
        var times: MutableList<String>
    ) : RecyclerView.Adapter<ManyAdapter.ManyHolder>(){

        inner class ManyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManyHolder {
            return ManyHolder(
                layoutInflater.inflate(R.layout.single_date, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ManyHolder, position: Int) {
            holder.itemView.apply {
                val button = findViewById<Button>(R.id.single_date_btn)
                val text = findViewById<TextView>(R.id.single_date_text)
                text.text = times[position]
                button.setOnClickListener {
                    val cal : Calendar = Calendar.getInstance()
                    val setTimeListener = TimePickerDialog.OnTimeSetListener{
                            _, pickedHour, pickedMinute ->
                        val hour = String.format("%02d", pickedHour);
                        val minute = String.format("%02d", pickedMinute)
                        text.text = "$hour:$minute"
                        times[position] =  "$hour:$minute"
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
            }
        }

        override fun getItemCount(): Int {
            return times.size
        }

        fun makeSize(size: Int) {
            if (size < times.size) {
                times = times.filterIndexed { i, _ ->
                    if (i >= size && eventCreator.event.alarmId != 0) {
                        // delete alarm if it exists
                        val alarmIntent = Intent(context, AlarmReceiver::class.java)
                        val pendingIntent = PendingIntent.getBroadcast(
                            context,
                            eventCreator.event.alarmId + i,
                            alarmIntent,
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        pendingIntent?.let { _pendingIntent->
                            alarmManager.cancel(_pendingIntent)
                        }
                    }
                    i < size
                }.toMutableList()

            } else {
                for(i in 0 until size - times.size){
                    times.add("00:00")
                }
            }
            notifyDataSetChanged()
        }
    }
}