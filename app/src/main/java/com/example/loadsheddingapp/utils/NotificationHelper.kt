package com.example.loadsheddingapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.loadsheddingapp.R

// NotificationHelper manages real-time push and local alert notifications.
// It creates notification channels on Android 8.0+ and safely checks runtime permissions.
object NotificationHelper {

    const val CHANNEL_ID = "loadshedding_alerts_channel"
    private const val CHANNEL_NAME = "Load-Shedding Stage Alerts"
    private const val CHANNEL_DESCRIPTION = "Real-time notifications for load-shedding stage updates and schedule reminders"

    // Creates the Notification Channel required on Android 8.0 (API 26) and above.
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Displays a real-time notification alert when a suburb's load-shedding stage changes.
    fun showStageChangeNotification(context: Context, suburbName: String, newStage: Int) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannel(context)

        val title = "Real-Time Stage Update: $suburbName"
        val message = if (newStage == 0) {
            "Great news! Load-shedding has been suspended for $suburbName."
        } else {
            "Notice: $suburbName is now on Stage $newStage."
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), builder.build())
    }

    // Displays a schedule reminder notification before a slot starts.
    fun showScheduleReminderNotification(context: Context, suburbName: String, startTime: String, stage: Int) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannel(context)

        val title = "Upcoming Load-Shedding Reminder"
        val message = "Stage $stage load-shedding starts at $startTime for $suburbName."

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), builder.build())
    }

    // Safely checks whether notification posting permission is granted.
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}
