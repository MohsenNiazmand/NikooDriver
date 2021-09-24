

package com.example.nikoodriver.feature.home

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.nikoodriver.R
import com.example.nikoodriver.common.BaseActivity
import com.example.nikoodriver.feature.auth.login.LoginActivity
import com.example.nikoodriver.feature.home.credit.CreditDialog
import com.example.nikoodriver.feature.current_travel.CurrentTravelActivity
import com.example.nikoodriver.feature.declined_passengers.DeclinedPassengersActivity
import com.example.nikoodriver.feature.travel_registeration.TravelRegistrationActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomeActivity : BaseActivity() {
    val sharedPreferences: SharedPreferences by inject()

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

    @SuppressLint("CutPasteId", "BinaryOperationInTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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

        }

        deActiveBtn.setOnClickListener {
            activationTv.visibility= View.VISIBLE
            activeBtn.visibility= View.VISIBLE
            deActiveBtn.visibility=View.GONE

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



//        Firebase.messaging.getToken().addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Timber.i("Fetching FCM registration token failed"+ task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            val msg = token
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        })
//        // [END log_reg_token]
//        Toast.makeText(applicationContext, "See README for setup instructions", Toast.LENGTH_SHORT).show()







    }





}