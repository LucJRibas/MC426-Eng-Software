package com.project.lembretio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class EventActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        editText = findViewById(R.id.etEventTitle)
        submitButton = findViewById(R.id.btnSubmit)
        cancelButton = findViewById(R.id.btnCancel)

        submitButton.setOnClickListener {
            val intentBack = Intent(applicationContext, MainActivity::class.java)

            val title = editText.text.toString()
            if (title.isNotEmpty()){
                EventApplication.adapter?.addEvent(Event(title, false))
            }

            startActivity(intentBack)
        }

        cancelButton.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }


    }
}