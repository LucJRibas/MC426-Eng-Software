package com.project.lembretio


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
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
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.Calendar

class EventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val eventViewModel: EventViewModel by viewModels {
        EventModelFactory((application as EventApplication).repository)
    }
    private lateinit var mainText: TextView
    private lateinit var notifyButton: Button
    private lateinit var dateText: TextView
    private lateinit var editText: EditText
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button
    private lateinit var setDateButton: Button

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        createToolBar()
        findGuiElementsById()
        setupGuiElements()
    }

    private fun createToolBar() {
        // add toolbar with back button
        val toolbar = findViewById<Toolbar>(R.id.tbEvent)
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }

    private fun findGuiElementsById(){
        mainText = findViewById(R.id.rvEventActivityTitle)
        dateText = findViewById(R.id.textEventDate)
        editText = findViewById(R.id.etEventTitle)
        submitButton = findViewById(R.id.btnSubmit)
        cancelButton = findViewById(R.id.btnCancel)
        setDateButton = findViewById(R.id.btnSaveDate)
    }

    @SuppressLint("ScheduleExactAlarm")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setupGuiElements() {
        val initialTitle = intent.getStringExtra("title")
        val eventId = intent.getIntExtra("event_id", -1)
        val initialDate = intent.getStringExtra("date")
        var alarmId = intent.getIntExtra("alarm_id", 0)

        if (initialTitle != null) {
            editText.setText(initialTitle)
            mainText.text = "Edit Event Here..."
        }

        if(initialDate != null) {
            dateText.text = initialDate
        }

        if(alarmId == 0) {
            alarmId = Math.toIntExact(LocalDateTime.now().getLong(ChronoField.EPOCH_DAY))
        }

        setSubmitButton(initialTitle, eventId, alarmId)
        setCancelButton()
        setDateButton()

    }

    private fun setSubmitButton(initialTitle: String?, eventId: Int, alarmId: Int) {
        submitButton.setOnClickListener {

            val title = editText.text.toString()
            val dateString = dateText.text.toString()

            if (dateString.isNotEmpty()) {
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                val date = LocalDateTime.parse(dateString, formatter)
                if (title.isNotEmpty()) {
                    val intentBack = Intent(applicationContext, MainActivity::class.java)

                    when (initialTitle) {
                        null -> insertDataToDatabase(title, date, alarmId)
                        else -> updateDatabaseEvent(title, date, alarmId, eventId)
                    }

                    scheduleAlarm(title, date, alarmId)

                    startActivity(intentBack)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please Enter A Title For Your Event",
                        Toast.LENGTH_SHORT
                    ).show()   //shows the toast if input field is empty
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please Enter A Date For Your Event",
                    Toast.LENGTH_SHORT
                ).show()   //shows the toast if input field is empty
            }
        }
    }

    private fun setCancelButton() {
        cancelButton.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }

    private fun setDateButton() {
        setDateButton.setOnClickListener {
            val cal : Calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(this,this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.setCancelable(false)
            datePickerDialog.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val cal : Calendar = Calendar.getInstance()
        val day = String.format("%02d", dayOfMonth);
        val month = String.format("%02d", month + 1);
        val year = String.format("%04d", year)
        val lastDate = dateText.text
        dateText.text = "$day-$month-$year"
        val timePickerDialog = TimePickerDialog(this,this,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true)
        timePickerDialog.setOnCancelListener {
            dateText.text = lastDate
            Toast.makeText(
                applicationContext,
                "A data foi não pode ser definida.",
                Toast.LENGTH_SHORT
            ).show()   //shows the toast if input field is empty
        }
        timePickerDialog.setCancelable(false)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val hour = String.format("%02d", hourOfDay);
        val minute = String.format("%02d", minute)
        dateText.text = "${dateText.text} $hour:$minute"
    }

    private fun insertDataToDatabase(title : String, date: LocalDateTime, alarmId: Int){
        val createdEvent = Event(title,false, date, alarmId)
        eventViewModel.addEvent(createdEvent)
        Toast.makeText(applicationContext, "Successfully Added!", Toast.LENGTH_SHORT).show()
    }

    private fun updateDatabaseEvent(title : String, date: LocalDateTime, alarmId: Int, id: Int){
        val updatedEvent = Event(title, true, date, alarmId, id)
        eventViewModel.updateEvent(updatedEvent)
        Toast.makeText(applicationContext, "Successfully Updated!", Toast.LENGTH_SHORT).show()
    }

    private fun scheduleAlarm(title: String, date: LocalDateTime, alarmId: Int){
        var alarmIntent = Intent(applicationContext, AlarmReceiver::class.java)
        alarmIntent.putExtra("title", title)
        alarmIntent.putExtra("text", date.toString())
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            alarmId,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            date.atZone(ZoneId.of("America/Sao_Paulo")).toInstant().toEpochMilli(),
            pendingIntent
        )
    }
}