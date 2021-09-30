

package com.akaf.nikoodriver.feature.home

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.PowerManager
import android.view.View
import android.widget.Toast
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseActivity
import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.data.location.SendLocation
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.feature.home.credit.CreditDialog
import com.akaf.nikoodriver.feature.current_travel.CurrentTravelActivity
import com.akaf.nikoodriver.feature.declined_passengers.DeclinedPassengersActivity
import com.akaf.nikoodriver.feature.travel_registeration.TravelRegistrationActivity
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject

class HomeActivity : BaseActivity() {
    val compositeDisposable=CompositeDisposable()
    val sharedPreferences: SharedPreferences by inject()
    val hiveMqttManager:HiveMqttManager by inject()
    val homeViewModel:HomeViewModel by inject()
    private lateinit var wakeLock: PowerManager.WakeLock
    private var fusedLocation: Location? = null
    var isFastLocation = false
    lateinit var fusedLocationClient: FusedLocationProviderClient


    private fun initData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()
        //we check tokenExistence ,if it exist user goes to home
        val tokenExistence=sharedPreferences.getString("access_token", null)
        if (tokenExistence==null){
            finish()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            overridePendingTransition(0, 0);
        }else{
            sendFireBaseToken()
        }
    }

    @SuppressLint("CutPasteId", "BinaryOperationInTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        wakeLockSetup()
        initData()
//        if (sharedPreferences.getString("driverId",null)!=null){
//            hiveMqttManager.subscribeToChatDriver(sharedPreferences.getString("driverId",null)!!.toInt())
//        }

        if (!CheckInternet()){
            val snackBar = Snackbar
                .make(
                    findViewById(R.id.homeRoot),
                    "اینترنت متصل نیست",
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction("بررسی مجدد ...") { view: View? -> finish();
                    startActivity(intent); }
            snackBar.show()
        }

        if (!CheckGps()){
            val snackBar = Snackbar
                .make(
                    findViewById(R.id.homeRoot),
                    "مکان یاب خود را روشن نمایید",
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction("بررسی مجدد ...") { view: View? -> finish();
                    startActivity(intent); }
            snackBar.show()
        }




        activeBtn.setOnClickListener {
            activationTv.visibility= View.GONE
            activeBtn.visibility= View.GONE
            deActiveBtn.visibility=View.VISIBLE
            hiveMqttManager.connect()
            startLocationUpdates()


        }

        deActiveBtn.setOnClickListener {
            activationTv.visibility= View.VISIBLE
            activeBtn.visibility= View.VISIBLE
            deActiveBtn.visibility=View.GONE
            hiveMqttManager.disconnect()

        }

        creditBtn.setOnClickListener {
            val creditDialog = CreditDialog()
            creditDialog.show(supportFragmentManager, null)

        }

        travelRegistrationBtn.setOnClickListener {
            startActivity(Intent(this@HomeActivity, TravelRegistrationActivity::class.java))

        }

        declinedPassengersBtn.setOnClickListener {
            startActivity(Intent(this@HomeActivity, DeclinedPassengersActivity::class.java))

        }

        currentTravelBtn.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CurrentTravelActivity::class.java))

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = "fcm_id"
            val channelName = "fcm_channel"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }


    }


    var locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            if (p0 != null) {
                runOnUiThread {
                    fusedLocation = p0.lastLocation
                    val sendLocation = SendLocation()
                    sendLocation.location.add(fusedLocation!!.longitude)
                    sendLocation.location.add(fusedLocation!!.latitude)
                    homeViewModel.sendDriverLocation(fusedLocation!!)
//                    homeViewModel.sendDriverLocationToRest(sendLocation)
//                    checkFastLocUpdate(homeViewModel.currentTripLiveData.value)
                }

            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
        }
    }

    //needs trip body
//    private fun checkFastLocUpdate(trip: Trip?) {
//        if (trip == null || trip.status == "trip_ended") {
//            if (isFastLocation) {
//                isFastLocation = false
//                locationRequest = LocationRequest.create().apply {
//                    smallestDisplacement = 100f
//                    interval = 30 * 60 * 1000
//                    this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
//                }
//                stopLocationUpdates()
//                startLocationUpdates()
//            }
//            return
//
//        }
//        if (homeViewModel.checkNeedFastLocUpdate(trip.sources, trip.destinations, fusedLocation)) {
//            if (!isFastLocation) {
//                isFastLocation = true
//                locationRequest = LocationRequest.create().apply {
//                    smallestDisplacement = 100f
//                    interval = 30 * 1000
//                    // interval = 30*1000
//                    this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
//                }
//                stopLocationUpdates()
//                startLocationUpdates()
//
//            }
//        } else {
//            if (isFastLocation) {
//                isFastLocation = false
//                locationRequest = LocationRequest.create().apply {
//                    smallestDisplacement = 100f
//                    interval = 30 * 60 * 1000
//                    this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
//                }
//                stopLocationUpdates()
//                startLocationUpdates()
//            }
//        }
//    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {

        if (!CheckGps()) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    var locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 60 * 30 * 1000
        smallestDisplacement = 100f
        this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }


    private fun sendFireBaseToken() {
        FirebaseMessaging.getInstance().subscribeToTopic("Drivers")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener
            val token = task.result
            homeViewModel.sendFcmToken(token)
        })
    }


    private fun wakeLockSetup() {
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Nikoo::PassengerLock").apply {
                    acquire(10 * 60 * 1000L /*10 minutes*/)
                }
            }
    }

    private fun destroyWakeLock() {
        when {
            wakeLock.isHeld -> wakeLock.release()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        destroyWakeLock()
    }



}