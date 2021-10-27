package com.akaf.nikoodriver.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.feature.home.HomeActivity
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import com.google.android.gms.location.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class DriverForegroundService : Service() {
    val CHANNEL_ID="NikooDriver"
    val hiveMqtt:HiveMqttManager by inject()
    private lateinit var wakeLock: PowerManager.WakeLock
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private var fusedLocation: Location?=null
    var locationCallback = object : LocationCallback() {
        @SuppressLint("BinaryOperationInTimber")
        override fun onLocationResult(p0: LocationResult?) {
            if (p0 != null) {
                fusedLocation = p0.lastLocation
                val sendLocation = SendLocation()
                sendLocation.location.add(fusedLocation!!.latitude)
                sendLocation.location.add(fusedLocation!!.longitude)
                hiveMqtt.publishDriverLocation(fusedLocation!!)

            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
        }

    }





    override fun startService(service: Intent?): ComponentName? {
        return super.startService(service)
    }


    companion object {
        var isUserOnline =  false
        var instance : DriverForegroundService? =null
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, DriverForegroundService::class.java)
            isUserOnline=true
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, DriverForegroundService::class.java)
            isUserOnline=false
            context.stopService(stopIntent)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        wakeLockSetup()
        instance = this
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        fusedLocationClient.removeLocationUpdates(locationCallback)

        val locationRequest =  if (isUserOnline) {
            LocationRequest.create().apply {
                interval = 3000
                this.fastestInterval = 2000
                this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                this.smallestDisplacement = 0f
            }
        }else{
            LocationRequest.create().apply {
                interval = 30000
                this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                this.smallestDisplacement = 100f
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val notificationIntent = Intent(this, HomeActivity::class.java)
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0, notificationIntent, 0
//        )


        // Create an Intent for the activity you want to start
        val resultIntent = Intent(this, HomeActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, 0)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("سرویس نیکو همراه")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(resultPendingIntent)
            .build()

        startForeground(1, notification)
        startLocationUpdates()

//        hiveMqtt.connect()
        return START_NOT_STICKY
    }


    private fun wakeLockSetup(){
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Nikoo::DriverLock").apply {
                    acquire(1000*60*1000L /*10 minutes*/)
                }
            }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    override fun onDestroy() {
        super.onDestroy()
        instance = null
        stopLocationUpdates()
        wakeLock.release()
    }

}