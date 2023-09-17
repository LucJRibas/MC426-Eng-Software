package com.project.lembretio


import android.Manifest
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar


class EventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var mainText: TextView
    private lateinit var notifyButton: Button
    private lateinit var dateText: TextView
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
        dateText = findViewById(R.id.textEventDate)
        editText = findViewById(R.id.etEventTitle)
        submitButton = findViewById(R.id.btnSubmit)
        cancelButton = findViewById(R.id.btnCancel)
        notifyButton = findViewById(R.id.btnNotify)

        val initialTitle = intent.getStringExtra("title")
        val eventIdx = intent.getIntExtra("idx", -1)
        val initialDate = intent.getStringExtra("date")

        if (initialTitle != null) {
            editText.setText(initialTitle)
            mainText.text = "Edit Event Here..."
        }

        if(initialDate != null) {
            dateText.setText(initialDate)
        }

        submitButton.setOnClickListener {

            val title = editText.text.toString()
            var date = dateText.text.toString()
            if (title.isNotEmpty()){
                val intentBack = Intent(applicationContext, MainActivity::class.java)

                when(initialTitle) {
                    null -> EventApplication.adapter?.addEvent(Event(title, false))
                    else -> {
                        EventApplication.adapter?.changeEventTitle(eventIdx, title)
                        EventApplication.adapter?.changeEventDate(eventIdx, date)
                    }
                }

                startActivity(intentBack)
            } else {
                Toast.makeText(applicationContext, "Please Enter A Title For Your Event", Toast.LENGTH_SHORT).show()   //shows the toast if input field is empty
            }

        }

        cancelButton.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        pickDate()

        notifyButton.setOnClickListener {
            var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("Lembretio")
                .setContentText("muhahahahahah")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            var notificationManagerCompat = NotificationManagerCompat.from(this)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 122);
            }

            notificationManagerCompat.notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MyNotification"
            val descriptionText = "kkkkkkkkk"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("idchannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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
        getTimeCalendar()
        setDateButton = findViewById(R.id.btnSaveDate)
        setDateButton.setOnClickListener {
            DatePickerDialog(this,this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year
        dateText.setText(dayOfMonth.toString() + "-" + (month + 1) + "-" + year)
        TimePickerDialog(this,this,hour,minute,true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
        dateText.setText(dateText.text.toString() + " " +  hourOfDay + ':' + minute)
    }
}