package com.azamovhudstc.eventapp.service

import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.azamovhudstc.eventapp.MainActivity
import com.azamovhudstc.eventapp.R
import timber.log.Timber
import com.azamovhudstc.eventapp.broadcast.EventsReceiver

private const val CHANNEL = "Events App"

class EventsService : Service() {

    private val receiver = EventsReceiver()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return if (intent?.extras?.get("STOP") != "STOP") {
            val events = intent?.extras?.getStringArrayList("enabledActions")
            Timber.d("onStartCommand events: $events")
            registerReceiver(receiver, IntentFilter().apply {
                for (i in events?.indices!!) {
                    addAction(events[i])
                }
            })
            // TODO why we need start not sticky or start sticky
            START_NOT_STICKY
        } else {
            stopSelf()
            START_NOT_STICKY
        }
    }

    override fun onDestroy() {
        receiver.clearReceiver()
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    "Events App",
                    CHANNEL,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            channel.enableVibration(false)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startService() {
        val notifyIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val notifyPendingIntent = PendingIntent.getActivity(
            this,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat
            .Builder(this, CHANNEL)
            .setContentIntent(notifyPendingIntent)
            .setSilent(true)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(resources.getString(R.string.app_name))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(createNotificationLayout())
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationLayout(): RemoteViews {
        val view = RemoteViews(packageName, R.layout.remote_view)
        view.setOnClickPendingIntent(R.id.remoteButtonClose, createPendingIntent())
        return view
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, EventsService::class.java)
        intent.putExtra("STOP", "STOP")
        return PendingIntent.getService(
            this,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}