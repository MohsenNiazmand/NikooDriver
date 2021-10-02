package com.akaf.nikoodriver.services.firebase

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Intent
import com.akaf.nikoodriver.feature.home.HomeActivity
import com.google.firebase.messaging.RemoteMessage


class NikooFirebase : FirebaseMessagingService() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification=NotificationCompat.Builder(applicationContext,"NikooDriver")
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setSmallIcon(android.R.drawable.ic_menu_month)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1001,notification)

    }

    override fun onNewToken(token: String) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }



    private fun sendRegistrationToServer(token: String) {
        // TODO: Implement this method to send token to your app server.
    }


}