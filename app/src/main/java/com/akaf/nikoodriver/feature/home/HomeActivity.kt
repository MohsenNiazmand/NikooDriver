

package com.akaf.nikoodriver.feature.home

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.View
import android.widget.Toast
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseActivity
import com.akaf.nikoodriver.data.TokenContainer
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.feature.home.credit.CreditDialog
import com.akaf.nikoodriver.feature.current_travel.CurrentTravelActivity
import com.akaf.nikoodriver.feature.declined_passengers.DeclinedPassengersActivity
import com.akaf.nikoodriver.feature.travel_registeration.TravelRegistrationActivity
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
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