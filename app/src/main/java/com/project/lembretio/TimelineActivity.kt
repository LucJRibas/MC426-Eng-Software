package com.project.lembretio



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class TimelineActivity : AppCompatActivity() {

    private fun countDoses(event: Event): Int {
        return 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        // Receba o evento da Intent
        val receivedEvent: Event? = intent.getParcelableExtra("event")


        val titleText = findViewById<TextView>(R.id.remedio)
        val dateinit = findViewById<TextView>(R.id.inicio)
        val timing = findViewById<TextView>(R.id.timing)
        val progresso = findViewById<TextView>(R.id.progresso)
        val alarme = findViewById<TextView>(R.id.alarme)

        val btn_edit = findViewById<Button>(R.id.btn_time_edit)
        val btn_del = findViewById<Button>(R.id.btn_time_del)
        val btn_prev = findViewById<Button>(R.id.btn_time_prev)

        if (receivedEvent != null) {
            titleText.text = receivedEvent.name
            dateinit.text = "Data de início: ${receivedEvent.createdDateFormatted}"

            if (receivedEvent.repeating) {
                timing.text = "Horários do dia: ${receivedEvent.times.joinToString(separator = ", ") { it.toString() }}"
                progresso.text = "Doses tomadas: ${countDoses(receivedEvent)}"
            }
        }

       btn_prev.setOnClickListener {
            val intentBack = Intent(applicationContext, MainActivity::class.java)
            startActivity(intentBack)
        }

    }
}