

package com.akaf.nikoodriver.feature.main.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseActivity
import com.akaf.nikoodriver.data.responses.mqttTripResponse.TripData
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_declined_passenger.*
import org.koin.android.ext.android.inject
import androidx.recyclerview.widget.PagerSnapHelper

import androidx.recyclerview.widget.SnapHelper
import com.akaf.nikoodriver.data.responses.updateResponse.UpdateData
import com.akaf.nikoodriver.feature.main.TripsAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import timber.log.Timber


@KoinApiExtension
class HomeActivity : BaseActivity(), TripsAdapter.CartItemViewCallBacks {
    var tripsAdapter= TripsAdapter()
    val compositeDisposable=CompositeDisposable()
    val sharedPreferences: SharedPreferences by inject()
    val homeViewModel: HomeViewModel by viewModel()
    private lateinit var wakeLock: PowerManager.WakeLock
    val tripsList=ArrayList<TripData>()
    val token=sharedPreferences.getString("token", null)
    val refreshToken=sharedPreferences.getString("refresh_token", null)
    var vibrator: Vibrator? = null
   lateinit var mediaPlayer:MediaPlayer
    lateinit var pattern: LongArray

    override fun onStart() {
        super.onStart()
        homeViewModel.versionAppLiveData.observe(this){
            if (it.outDated){
                if (it.forceUpdate){
                    initForceUpdate(it)
                }else{
                    initUpdate(it)
                }
            }
        }
    }

    @KoinApiExtension
    @SuppressLint("CutPasteId", "BinaryOperationInTimber", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
        pattern = longArrayOf(0, 1000, 1000)
        wakeLockSetup()
        mediaPlayer=MediaPlayer.create(this,R.raw.service_alarm)



        if (!CheckInternet()){
            val snackBar = Snackbar
                .make(
                    findViewById(R.id.homeRoot),
                    "اینترنت متصل نیست",
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction("بررسی مجدد ...") {
                    finish()
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
                .setAction("بررسی مجدد ...") {
                    finish()
                    startActivity(intent); }
            snackBar.show()
        }




        //checks tokenExistence ,if it exist user goes to home

        if (token==null && refreshToken==null){
            finish()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            overridePendingTransition(0, 0)
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
            vibrator!!.vibrate(pattern,0)
            mediaPlayer.start()
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
        vibrator!!.cancel()
        mediaPlayer.stop()
        mediaPlayer=MediaPlayer.create(this,R.raw.service_alarm)
        destroyWakeLock()
    }

    @KoinApiExtension
    override fun onRejectBtnClicked(tripData: TripData) {
        homeViewModel.rejectTrip(tripData.id)
        handleTrips(tripData)

    }

    @KoinApiExtension
    override fun onAcceptBtnClicked(tripData: TripData) {
        homeViewModel.acceptTrip(tripData.id,-1)
        handleTrips(tripData)

    }

    override fun timerFinished(tripData: TripData) {
        handleTrips(tripData)

    }

    @SuppressLint("CommitPrefEdits")
    private fun handleTrips(tripData: TripData){

        if (tripsList.size==0){
            tripView.visibility=View.GONE
            tripsList.remove(tripData)
            vibrator!!.cancel()
            mediaPlayer.stop()
            mediaPlayer=MediaPlayer.create(this,R.raw.service_alarm)
            val navController = findNavController(R.id.fragmentContainerView)
            navController.navigate(R.id.homeFragment)
        }else if (tripsList.size>0){
            tripsAdapter.removeTripFromList(tripData)
            rvTrips.smoothScrollToPosition(tripsAdapter.trips.size)
            tripsList.remove(tripData)
        }

    }


    private fun initForceUpdate(updateData: UpdateData) {
        showForceUpdateDialog(updateData)
    }

    private fun initUpdate(updateData: UpdateData) {
        showUpdateDialog(updateData)
    }


    private fun showUpdateDialog(updateData: UpdateData) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_update)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        (dialog.findViewById<View>(R.id.yesBtnUpdate) as Button).setOnClickListener {
            dialog.dismiss()
            update(updateData.downloadURI)
            finishAffinity()
        }
        (dialog.findViewById<View>(R.id.noBtnUpdate) as Button).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showForceUpdateDialog(updateData: UpdateData) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_force_update)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        (dialog.findViewById<View>(R.id.yesBtnForceUpdate) as Button).setOnClickListener {
            dialog.dismiss()
            update(updateData.downloadURI)
            finishAffinity()
        }
        (dialog.findViewById<View>(R.id.noBtnForceUpdate) as Button).setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }
        dialog.show()
    }

    private fun update(url: String) {
        try {
            initUpdateUri(url)
        } catch (ange: ActivityNotFoundException) {
            initUpdateGooglePlay()
        }
    }

    private fun initUpdateUri(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun initUpdateGooglePlay() {
        try {
            val appPackageName: String =packageName
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        } catch (e: java.lang.Exception) {

        }
    }



}