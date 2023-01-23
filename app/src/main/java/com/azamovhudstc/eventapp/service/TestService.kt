package com.azamovhudstc.eventapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.azamovhudstc.eventapp.MainActivity
import com.azamovhudstc.eventapp.R
import timber.log.Timber
import com.azamovhudstc.eventapp.broadcast.TestReceiver

private const val CHANNEL = "Events App"

class TestService : Service() {

    private val testReceiver = TestReceiver()
    private var actionScreeOn = ""
    private var actionScreeOff = ""

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return if (intent?.extras?.get("STOP") != "STOP") {
//            Timber.d("onStartCommand: ${intent?.extras?.get("START_SERVICE")}")
//            Timber.d("onStartCommand: ${intent?.action}")

            val extras = intent?.extras?.get("START_SERVICE")

            when (intent?.action) {
                "SCREEN_ON" -> {
                    actionScreeOn = if (extras == true) Intent.ACTION_SCREEN_ON
                    else ""
                }
                else -> {
                    actionScreeOff = if (extras == true) Intent.ACTION_SCREEN_OFF
                    else ""
                }
            }

            Timber.d("onStartCommand actionScreenOn: $actionScreeOn")
            Timber.d("onStartCommand actionScreenOff: $actionScreeOff")

            if (actionScreeOn.isEmpty() && actionScreeOff.isEmpty()) unregisterReceiver(testReceiver)

            this.unregisterReceiver(testReceiver)

            registerReceiver(testReceiver, IntentFilter().apply {
                addAction(actionScreeOn)
                addAction(actionScreeOff)
            })

            START_NOT_STICKY
        } else {
            stopSelf()
            START_NOT_STICKY
        }

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
            .setSmallIcon(R.drawable.ic_logo)
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

    override fun onDestroy() {
        super.onDestroy()

    }
}