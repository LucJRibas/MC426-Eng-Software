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
            val title = intent.getStringExtra("title")
            val eventId = intent.getIntExtra("event_id", -1)
            val date = intent.getStringExtra("date")
            val alarmId = intent.getIntExtra("alarm_id", 0)
            val repeating = intent.getBooleanExtra("repeating", false)
            val uri: Uri? = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)


            createNotificationChannel(it)
            playRingTone(it, uri)
            if (repeating) rescheduleAlarm(it, title, date, eventId, alarmId, uri)
            showNotification(it, title, date, eventId, alarmId, uri)
        }
    }

    private fun showNotification(context: Context, title: String?, date: String?, eventId: Int, alarmId: Int, uri: Uri?){
        //TODO ir para histórico -> Deletar evento?
        val eventIntent = Intent(context, MainActivity::class.java)
        eventIntent.component = ComponentName(context.packageName, MainActivity::class.java.name)
        eventIntent.putExtra("title", title)
        eventIntent.putExtra("event_id", eventId)
        eventIntent.putExtra("date", date)
        eventIntent.putExtra("alarm_id", alarmId)

        eventIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val text = if (uri == null) date else "$date Aperte nessa notificação para parar o alarme"

        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setContentTitle(title)
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

    private fun rescheduleAlarm(context: Context, title: String?, date: String?, eventId: Int, alarmId: Int, uri: Uri?) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val newDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")).plusDays(1)

        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra("title", title)
        alarmIntent.putExtra("event_id", eventId)
        alarmIntent.putExtra("date", newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
        alarmIntent.putExtra("alarm_id", alarmId)
        alarmIntent.putExtra("repeating", true)
        alarmIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, uri)

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