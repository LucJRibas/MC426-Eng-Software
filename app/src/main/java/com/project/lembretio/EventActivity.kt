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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        editText = findViewById(R.id.etEventTitle)
        submitButton = findViewById(R.id.btnSubmit)

        submitButton.setOnClickListener {
            val intentBack = Intent(applicationContext, MainActivity::class.java)
            intentBack.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            val title = editText.text.toString()
            if (title.isNotEmpty()){
                intentBack.putExtra("event", title)
            }

            startActivity(intentBack)
        }


    }
}