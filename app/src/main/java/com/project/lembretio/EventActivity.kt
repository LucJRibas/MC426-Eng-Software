package com.project.lembretio

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.Calendar


class EventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var mainText: TextView
    private lateinit var editText: EditText
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button
    private lateinit var setDateButton: Button

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0


    private val appDatabase by lazy { AppDatabase.getDatabase(this).EventDao() }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        // add toolbar with back button
        val toolbar = findViewById<Toolbar>(R.id.tbEvent)
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        mainText = findViewById(R.id.rvEventActivityTitle)
        editText = findViewById(R.id.etEventTitle)
        submitButton = findViewById(R.id.btnSubmit)
        cancelButton = findViewById(R.id.btnCancel)

        val initialTitle = intent.getStringExtra("title")
        val eventIdx = intent.getIntExtra("idx", -1)

        if (initialTitle != null) {
            editText.setText(initialTitle)
            mainText.text = "Edit Event Here..."
        }

        submitButton.setOnClickListener {

            val title = editText.text.toString()
            if (title.isNotEmpty()){
                val intentBack = Intent(applicationContext, MainActivity::class.java)

                when(initialTitle) {
                    null -> EventApplication.adapter?.addEvent(Event(title, false))
                    else -> EventApplication.adapter?.changeEventTitle(eventIdx, title)
                }

                startActivity(intentBack)
            } else {
                Toast.makeText(applicationContext, "Please Enter A Title For Your Event", Toast.LENGTH_SHORT).show()   //shows the toast if input field is empty
            }

            /*insertDataToDatabase(title)

             */



        }

        cancelButton.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        pickDate()

    }

    private fun getTimeCalendar() {
        val cal : Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        setDateButton = findViewById(R.id.btnSaveDate)
        setDateButton.setOnClickListener {
            DatePickerDialog(this, this, year, month, day).show()
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getTimeCalendar()

        TimePickerDialog(this,this,hour,minute,true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
    }

    private fun insertDataToDatabase(title : String){
        val createdEvent = Event(title,true)
        appDatabase.insert(createdEvent)
        Toast.makeText(applicationContext, "Successfully Added!", Toast.LENGTH_SHORT).show()


    }
}