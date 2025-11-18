package com.weylus.android.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.weylus.android.MainActivity
import com.weylus.android.R
import com.weylus.android.util.Constants
import dagger.hilt.android.AndroidEntryPoint

/**
 * Foreground service to maintain connection with Weylus server
 * This service runs while the app is connected to a server, ensuring
 * the connection is maintained even when the app is backgrounded.
 */
@AndroidEntryPoint
class WeylusService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start foreground service with notification
        val notification = createNotification("Initializing...")
        startForeground(Constants.NOTIFICATION_ID, notification)

        when (intent?.action) {
            Constants.ACTION_DISCONNECT -> {
                // Handle disconnect action
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cleanup resources
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                getString(R.string.service_notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.service_notification_channel_description)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(serverName: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val disconnectIntent = Intent(this, WeylusService::class.java).apply {
            action = Constants.ACTION_DISCONNECT
        }
        val disconnectPendingIntent = PendingIntent.getService(
            this,
            0,
            disconnectIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.service_notification_title))
            .setContentText(getString(R.string.service_notification_text, serverName))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_launcher_foreground,
                getString(R.string.disconnect),
                disconnectPendingIntent
            )
            .setOngoing(true)
            .build()
    }

    fun updateNotification(serverName: String) {
        val notification = createNotification(serverName)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Constants.NOTIFICATION_ID, notification)
    }
}
