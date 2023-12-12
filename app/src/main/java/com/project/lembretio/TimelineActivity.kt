package com.project.lembretio



import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class TimelineActivity : AppCompatActivity() {
    private val eventViewModel: EventViewModel by viewModels {
        EventModelFactory((application as EventApplication).repository)
    }
    private fun countDoses(event: Event): Int {
        return 0
    }

    private fun deleteEvent(event: Event) {

        val alarmIntent = Intent(applicationContext, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            event.alarmId,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntent?.let { _pendingIntent ->
            alarmManager.cancel(_pendingIntent)
        }

        eventViewModel.deleteEvent(event)


        Toast.makeText(applicationContext, "Evento ${event.name} excluído", Toast.LENGTH_SHORT).show()
        val intentBack = Intent(applicationContext, MainActivity::class.java)
        startActivity(intentBack)
    }
    private fun showDeleteConfirmationDialog(event: Event) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Excluir")
        builder.setMessage("Tem certeza que quer excluir ${event.name}?")

        builder.setPositiveButton("Sim") { dialogInterface: DialogInterface, i: Int ->
            deleteEvent(event)
        }

        builder.setNegativeButton("Não") { dialogInterface: DialogInterface, i: Int ->
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun configureButtons(receivedEvent: Event) {
        val btn_edit = findViewById<Button>(R.id.btn_time_edit)
        val btn_del = findViewById<Button>(R.id.btn_time_del)
        val btn_prev = findViewById<Button>(R.id.btn_time_prev)

        btn_edit.setOnClickListener {
            val intentEdit = Intent(applicationContext, EventPagerActivity::class.java)
            intentEdit.putExtra("event", receivedEvent)
            startActivity(intentEdit)
        }

        btn_del.setOnClickListener {
            showDeleteConfirmationDialog(receivedEvent)
        }

        btn_prev.setOnClickListener {
            val intentBack = Intent(applicationContext, MainActivity::class.java)
            startActivity(intentBack)
        }
    }

    private fun configureTexts(receivedEvent: Event){
        val titleText = findViewById<TextView>(R.id.remedio)
        val dateinit = findViewById<TextView>(R.id.inicio)
        val timing = findViewById<TextView>(R.id.timing)
        val progresso = findViewById<TextView>(R.id.progresso)
        val alarme = findViewById<TextView>(R.id.alarme)

        titleText.text = receivedEvent.name
        dateinit.text = "Data de início: ${receivedEvent.createdDateFormatted}"

        if (receivedEvent.repeating) {
            timing.text = "Horários do dia: ${receivedEvent.times.joinToString(separator = ", ") { it.toString() }}"
            progresso.text = "Doses tomadas: ${countDoses(receivedEvent)}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        val receivedEvent: Event? = intent.getParcelableExtra("event")

        if (receivedEvent != null) {
            configureTexts(receivedEvent)
            configureButtons(receivedEvent)
        }
    }
}