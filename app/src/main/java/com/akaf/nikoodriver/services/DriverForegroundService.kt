package com.akaf.nikoodriver.services

import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.feature.home.HomeActivity
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import org.koin.android.ext.android.inject

class DriverForegroundService : Service() {
    val CHANNEL_ID="NikooDriver"
    val hiveMqtt:HiveMqttManager by inject()

    override fun startService(service: Intent?): ComponentName? {
        return super.startService(service)
    }


    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, DriverForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        val input = intent?.getStringExtra("inputExtra")
        val notificationIntent = Intent(this, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        hiveMqtt.connect()
        return START_STICKY
    }
}