package com.project.lembretio

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        var ringtone: Ringtone? = null
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            handleAlarmData(context, it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handleAlarmData(context: Context?, intent: Intent) {
        context?.let {
            val event = intent.getParcelableExtra<Event>("event")
            val date = intent.getStringExtra("date")
            val alarmId = intent.getIntExtra("alarm_id", 0)


            createNotificationChannel(it)
            if (event != null) {
                playRingTone(it, event.uri)
                if (event.repeating) rescheduleAlarm(it, event, date, alarmId)
                showNotification(it, event, date, alarmId)
            }
        }
    }

    private fun showNotification(context: Context, event: Event, date: String?, alarmId: Int){
        //TODO ir para histórico -> Deletar evento?
        val eventIntent = Intent(context, TimelineActivity::class.java)
        eventIntent.component = ComponentName(context.packageName, TimelineActivity::class.java.name)
        eventIntent.putExtra("event", event)

        eventIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val text = if (event.uri == null) date else "$date Aperte nessa notificação para parar o alarme"

        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setContentTitle(event.name)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(PendingIntent.getActivity(
                context,
                alarmId,
                eventIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            ))


        with(NotificationManagerCompat.from(context)){
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
               TODO(reason = "Implementar Pedir a autorização")
            }
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            "CHANNEL_ID",
            "Channel Name",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableVibration(true);
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun playRingTone(context: Context, uri: Uri?) {
        if (uri != null) {
            ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone?.play()
        }

    }

    private fun rescheduleAlarm(context: Context, event: Event, date: String?, alarmId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val newDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")).plusDays(1)

        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra("event", event)
        alarmIntent.putExtra("date", newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
        alarmIntent.putExtra("alarm_id", alarmId)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            newDate.atZone(ZoneId.of("America/Sao_Paulo")).toInstant().toEpochMilli(),
            PendingIntent.getBroadcast(
                context,
                alarmId,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

    }
}