

package com.akaf.nikoodriver.feature.home

import android.Manifest
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
import android.util.Log
import android.view.View
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseActivity
import com.akaf.nikoodriver.data.location.SendLocation
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.feature.home.credit.CreditDialog
import com.akaf.nikoodriver.feature.current_travel.CurrentTravelFragment
import com.akaf.nikoodriver.feature.declined_passengers.DeclinedPassengersFragment
import com.akaf.nikoodriver.feature.travel_registeration.TravelRegistrationFragment
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_declined_passenger.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomeActivity : BaseActivity() {
    val compositeDisposable=CompositeDisposable()
    val sharedPreferences: SharedPreferences by inject()
    val hiveMqttManager: HiveMqttManager by inject()
    val homeViewModel: HomeViewModel by inject()
    private lateinit var wakeLock: PowerManager.WakeLock





    override fun onStart() {
        super.onStart()
        //we check tokenExistence ,if it exist user goes to home
        val tokenExistence=sharedPreferences.getString("access_token", null)
        if (tokenExistence==null){
            finish()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            overridePendingTransition(0, 0);
        }
    }

    @SuppressLint("CutPasteId", "BinaryOperationInTimber", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        wakeLockSetup()



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




        homeViewModel.newOfferLiveData.observe(this){
            newOfferView.visibility=View.VISIBLE
            offerDistanceTv.text=it.options?.disposalHour.toString()+"-"+it.options?.distance
            travelDistanceTv.text=it.startAt
            offerCostTv.text=it.cost
            originTv.text=it.startAt
            destinationTv.text=it.destinationCity
        }

        acceptOfferBtn.setOnClickListener {
            newOfferView.visibility=View.GONE
        }


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