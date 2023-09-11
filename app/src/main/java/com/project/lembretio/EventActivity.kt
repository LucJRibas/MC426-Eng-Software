package com.project.lembretio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class EventActivity : AppCompatActivity() {
    private lateinit var mainText: TextView
    private lateinit var editText: EditText
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

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

        }

        cancelButton.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }


    }
}