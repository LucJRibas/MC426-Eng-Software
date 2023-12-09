package com.project.lembretio

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            handleAlarmData(context, it)
        }
    }

    private fun handleAlarmData(context: Context?, intent: Intent) {
        context?.let {
            val title = intent.getStringExtra("title")
            val eventId = intent.getIntExtra("event_id", -1)
            val date = intent.getStringExtra("date")
            val alarmId = intent.getIntExtra("alarm_id", 0)

            createNotificationChannel(context = it)
            showNotification(context = it, title, date, eventId, alarmId)

        }
    }

    private fun showNotification(context: Context, title: String?, date: String?, eventId: Int, alarmId: Int){
        val eventIntent = Intent(context, EventActivity::class.java)
        eventIntent.component = ComponentName(context.packageName, EventActivity::class.java.name)
        eventIntent.putExtra("title", title)
        eventIntent.putExtra("event_id", eventId)
        eventIntent.putExtra("date", date)
        eventIntent.putExtra("alarm_id", alarmId)
        eventIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setContentTitle(title)
            .setContentText(date)
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
}