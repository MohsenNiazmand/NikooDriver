

package com.akaf.nikoodriver.feature.home

import android.annotation.SuppressLint

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PowerManager
import android.view.View
import android.widget.Toast
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseActivity
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
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
    val token=sharedPreferences.getString("token", null)
    val refreshToken=sharedPreferences.getString("refresh_token", null)




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




        //checks tokenExistence ,if it exist user goes to home

        if (token==null){
            finish()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            overridePendingTransition(0, 0);
        }

        //checks token expire
        else if (token!=null&& refreshToken!=null){
            homeViewModel.sendRefreshToken(token,refreshToken)
            homeViewModel.refreshTokenLiveData.observe(this){
                if (it.code()==403){
                    sharedPreferences.edit().clear().apply()
                    finish()
                    startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                    overridePendingTransition(0, 0);
                    Toast.makeText(applicationContext,"لطفا مجددا به حساب خود وارد شوید",Toast.LENGTH_SHORT).show()
                }
            }

        }





        homeViewModel.newOfferLiveData.observe(this){

            newOfferView.visibility=View.VISIBLE

            travelDistanceTv.text=it.options.distance.toString()+" "+"کیلومتر"
            offerCostTv.text=it.cost.toString()
            offerOrigin.text=it.sourceCity
            offerDestination.text=it.destinationCity
            val tripId=it.id
            timer()


            acceptOfferBtn.setOnClickListener {
                homeViewModel.acceptTrip(tripId,-1)
                newOfferView.visibility=View.GONE
            }


            rejectOfferBtn.setOnClickListener {
                homeViewModel.rejectTrip(tripId)
                newOfferView.visibility=View.GONE

            }

        }




    }


    private fun pollNewOffer() {
        val bundle = homeViewModel.offersQueue.poll()
        bundle?.let {
            homeViewModel.offerCountLiveData.postValue(homeViewModel.offersQueue.size)

        }

    }


    fun showOfferInQueue(bundle: Bundle) {

            homeViewModel.offersQueue.add(bundle)
            homeViewModel.offerCountLiveData.postValue(homeViewModel.offersQueue.size)

    }

    fun timer(){
        val timer = object: CountDownTimer(120000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli
                deadlineForReceivingTravelTv.text="$elapsedMinutes:$elapsedSeconds"
            }

            override fun onFinish() {
                newOfferView.visibility=View.GONE
            }
        }
        timer.start()
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