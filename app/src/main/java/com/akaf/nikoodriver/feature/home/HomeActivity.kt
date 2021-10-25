

package com.akaf.nikoodriver.feature.home

import android.annotation.SuppressLint

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.os.PowerManager
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseActivity
import com.akaf.nikoodriver.data.responses.mqttTripResponse.TripData
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_declined_passenger.*
import org.koin.android.ext.android.inject
import timber.log.Timber
import androidx.recyclerview.widget.PagerSnapHelper

import androidx.recyclerview.widget.SnapHelper
import com.akaf.nikoodriver.services.DriverForegroundService


class HomeActivity : BaseActivity(),TripsAdapter.CartItemViewCallBacks {
    var tripsAdapter=TripsAdapter()
    val compositeDisposable=CompositeDisposable()
    val sharedPreferences: SharedPreferences by inject()
    val homeViewModel: HomeViewModel by inject()
    private lateinit var wakeLock: PowerManager.WakeLock
    val tripsList=ArrayList<TripData>()
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
                    homeViewModel.clearSharedPreference()
                    applicationContext.cacheDir.deleteRecursively()
                    finish()
                    DriverForegroundService.stopService(applicationContext)
                    val intent=Intent(this@HomeActivity, LoginActivity::class.java).apply {
                        action = "com.package.ACTION_LOGOUT"
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }

                    startActivity(intent)


                    overridePendingTransition(0, 0);
                    Toast.makeText(applicationContext,"لطفا مجددا به حساب خود وارد شوید",Toast.LENGTH_SHORT).show()
                }
            }

        }
        val snapHelper: SnapHelper = PagerSnapHelper()
        rvTrips.layoutManager=LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        snapHelper.attachToRecyclerView(rvTrips)
        rvTrips.adapter=tripsAdapter

        homeViewModel.newOfferLiveData.observe(this){
            tripsList.add(it)
            tripsAdapter.cartItemViewCallBacks=this
            tripView.visibility=View.VISIBLE
            tripsAdapter.trips= tripsList
            val index=tripsAdapter.trips.size-1
            rvTrips.smoothScrollToPosition(index)
        }



    }


    private fun wakeLockSetup() {
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Nikoo::DriverLock").apply {
                    acquire(25 * 60 * 1000L /*25 minutes*/)
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

    override fun onRejectBtnClicked(tripData: TripData) {
        handleTrips(tripData)
        homeViewModel.rejectTrip(tripData.id)
    }

    override fun onAcceptBtnClicked(tripData: TripData) {
        handleTrips(tripData)
        homeViewModel.acceptTrip(tripData.id,-1)
    }

    override fun timerFinished(tripData: TripData) {
        handleTrips(tripData)
    }

    private fun handleTrips(tripData: TripData){
        Timber.i("TListSize"+tripsList.size.toString())
        if (tripsList.size==0){
            tripView.visibility=View.GONE
            tripsList.remove(tripData)
        }else if (tripsList.size>0){
            tripsAdapter.removeTripFromList(tripData)
            rvTrips.smoothScrollToPosition(tripsAdapter.trips.size)
            tripsList.remove(tripData)
        }
    }






}