package com.project.lembretio



import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class TimelineActivity : AppCompatActivity() {
    private val eventViewModel: EventViewModel by viewModels {
        EventModelFactory((application as EventApplication).repository)
    }


    private fun deleteEvent(event: Event) {
        event.times.forEachIndexed { i, dateTime ->
            val alarmIntent = Intent(applicationContext, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                event.alarmId + i,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager =
                applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            pendingIntent?.let { _pendingIntent ->
                alarmManager.cancel(_pendingIntent)
            }
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
        val layout = findViewById<LinearLayout>(R.id.info_LinearLayout)
        val titleText = findViewById<TextView>(R.id.remedio)
        val dateInit = findViewById<TextView>(R.id.inicio)
        val timing = findViewById<TextView>(R.id.timing)
        val progress = findViewById<TextView>(R.id.progresso)
        val alarm = findViewById<TextView>(R.id.alarme)
        val comment = findViewById<TextView>(R.id.comment)

        titleText.text = receivedEvent.name
        if (receivedEvent.isMedication) {
            dateInit.text = "Data de início: ${receivedEvent.createdDateFormatted}"
        } else {
            dateInit.text = "Data da consulta: ${receivedEvent.createdDateFormatted}"
            layout.removeView(progress)
        }


        if (receivedEvent.repeating) {
            timing.text = "Horários do dia: ${receivedEvent.times.joinToString(separator = ", ") { it.toString() }}"
            progress.text = "Doses tomadas: ${receivedEvent.countDoses()}"
        } else {
            timing.text = "Horário: ${receivedEvent.times.joinToString(separator = ", ") { it.toString() }}"
            layout.removeView(progress)
        }

        if (receivedEvent.uri == null) {
            layout.removeView(alarm)
        } else {
            val ringtone = RingtoneManager.getRingtone(applicationContext, receivedEvent.uri)
            alarm.text = "Alarme: ${ringtone.getTitle(applicationContext)}"
        }
        comment.text = "Comentários:\n${receivedEvent.comment}"

        if (!receivedEvent.isMedication) {
            val outerLayout = findViewById<ConstraintLayout>(R.id.event_ConstraintLayout)
            outerLayout.setBackgroundColor(Color.parseColor("#a3507b"))
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