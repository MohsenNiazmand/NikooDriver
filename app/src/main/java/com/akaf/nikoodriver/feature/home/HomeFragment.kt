package com.akaf.nikoodriver.feature.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.data.responses.profileResponse.ProfileResponse
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.feature.home.credit.CreditDialog
import com.akaf.nikoodriver.services.DriverForegroundService
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import retrofit2.Response
import timber.log.Timber

class HomeFragment : BaseFragment() {
    private val hiveMqttManager: HiveMqttManager by inject()
    val homeViewModel: HomeViewModel by inject()
    val sharedPreferences:SharedPreferences by inject()
    private var fusedLocation: Location? = null
    var isFastLocation = false
    lateinit var fusedLocationClient: FusedLocationProviderClient
    var mqttState:Boolean=true
    val compositeDisposable = CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    @SuppressLint("SetTextI18n", "BinaryOperationInTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userNameTv.text = homeViewModel.username


        var onlineStatus=sharedPreferences.getBoolean("isOnline",false)

        if (onlineStatus){
            active()


        }else
            deActive()

        val handler = Handler()
        handler.postDelayed({
            homeViewModel.getProfile()
        }, 1000)

        homeViewModel.profileLiveData.observe(viewLifecycleOwner){

            if (it!=null){

                val fname=it.fname
                val lname=it.lname
                    userNameTv.text= "$fname $lname"
                val capa=it.capacity
                    if (capa >0){
                        emptySeatsTv.visibility=View.VISIBLE
                        emptySeatsTv.text=it.capacity.toString()
                    }else if (capa == 0)
                        emptySeatsTv.visibility=View.GONE
                val openTrips=it.openTripsCount
                 if (openTrips>0 ){
                        unAcceptedPassengersTv.visibility=View.VISIBLE
                        unAcceptedPassengersTv.text= openTrips.toString()
                    } else if (openTrips==0)
                        unAcceptedPassengersTv.visibility=View.GONE
                val currents=it.currentTripsCount
                    if (currents>0){
                        currentTripsCountTv.visibility=View.VISIBLE
                        currentTripsCountTv.text=currents.toString()
                    }else if (currents==0){
                        currentTripsCountTv.visibility=View.GONE

                    }
                val rate=it.rate
                    rateTv.text=rate.toString()
                val credit=it.credit
                    creditTv.text=credit

            }





        }




            homeViewModel.mqttState.observe(viewLifecycleOwner) {
            if (it) {
                checkPermStartLocationUpdate()
                mqttState=it
                connectedSign.visibility=View.VISIBLE
                disconnectSign.visibility=View.GONE
            }
            else {
                mqttState=it
                connectedSign.visibility=View.GONE
                disconnectSign.visibility=View.VISIBLE
            }
        }


        logoutBtn.setOnClickListener {
            showLogoutDialog()

        }

        activeBtn.setOnClickListener {
            if(CheckInternet() && CheckGps())
            showEmptySeatsDialog()
            else
                Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()

        }

        deActiveBtn.setOnClickListener {
            deActive()
            homeViewModel.setEmptySeats(0,false)
        }

        creditBtn.setOnClickListener {
            val creditDialog = CreditDialog()
           creditDialog.show(childFragmentManager, null)

        }

        searchTravelBtn.setOnClickListener {
            if(CheckInternet() && CheckGps()){
                Navigation.findNavController(view)
                    .navigate(R.id.action_homeFragment_to_travelRegistrationFragment)
            }else
                Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()


        }

        unAcceptedPassengersBtn.setOnClickListener {
            if(CheckInternet() && CheckGps()){
                Navigation.findNavController(view)
                    .navigate(R.id.action_homeFragment_to_declinedPassengersFragment)
            }else
                Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()



        }

        currentTravelBtn.setOnClickListener {
            if(CheckInternet() && CheckGps()) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_homeFragment_to_currentTravelFragment)
            }else
                Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()

        }

        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }




    }


    private fun showLogoutDialog() {
        val logoutView = layoutInflater.inflate(R.layout.dialog_logout, null, false)
        val logoutDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        logoutDialog.setView(logoutView)
        logoutView.findViewById<MaterialButton>(R.id.logoutBtnYes).setOnClickListener {
            logoutDialog.dismiss()
            homeViewModel.clearSharedPreference()
            hiveMqttManager.disconnect()
            DriverForegroundService.stopService(requireContext())
            requireActivity().cacheDir.deleteRecursively()
            val intent=Intent(activity, LoginActivity::class.java).apply {
                action = "com.package.ACTION_LOGOUT"
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
        logoutView.findViewById<MaterialButton>(R.id.logoutBtnNo).setOnClickListener {
            logoutDialog.dismiss()
        }
        logoutDialog.show()
    }

    private fun active(){
        activationTv.visibility= View.GONE
        activeBtn.visibility= View.GONE
        deActiveBtn.visibility=View.VISIBLE
        emptySeatsTv.visibility=View.VISIBLE
        homeViewModel.setOnlineStatus(true)
        if (DriverForegroundService.instance==null)
            DriverForegroundService.startService(requireContext(),"Nikoo Driver")
    }

    private fun deActive(){
        activationTv.visibility= View.VISIBLE
        activeBtn.visibility= View.VISIBLE
        deActiveBtn.visibility=View.GONE
        currentTripsCountTv.visibility=View.GONE
        unAcceptedPassengersTv.visibility=View.GONE
        emptySeatsTv.visibility=View.GONE
        homeViewModel.setOnlineStatus(false)
        stopLocationUpdates()
        if (DriverForegroundService.instance!=null)
            DriverForegroundService.stopService(requireContext())






    }


    private fun showEmptySeatsDialog() {
        val emptySeatsView = layoutInflater.inflate(R.layout.dialog_empty_seats, null, false)
        val emptySeatsDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        emptySeatsDialog.setView(emptySeatsView)
        emptySeatsDialog.setCancelable(false)
        val emptySeatsEt= emptySeatsView.findViewById<EditText>(R.id.emptySeatsEt)
        emptySeatsView.findViewById<MaterialButton>(R.id.proceedEmptySeatsBtn).setOnClickListener {
            if (emptySeatsEt.text.isNotEmpty()){
                homeViewModel.setEmptySeats(emptySeatsEt.text.toString().toInt(),true)
                emptySeatsDialog.dismiss()
                homeViewModel.getProfile()
                active()
            }
        }
        emptySeatsView.findViewById<MaterialButton>(R.id.cancelEmptySeatsBtn).setOnClickListener {
            emptySeatsDialog.dismiss()
        }
        emptySeatsDialog.show()
    }


    fun checkPermStartLocationUpdate() {
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0?.areAllPermissionsGranted() == true) {
                        startLocationUpdates()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {

        if (!CheckGps()) {
            return
        }
        else if (CheckGps()){
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }

    var locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 60 * 30 * 1000
        smallestDisplacement = 100f
        this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    var locationCallback = object : LocationCallback() {
        @SuppressLint("BinaryOperationInTimber")
        override fun onLocationResult(p0: LocationResult?) {
            if (p0 != null) {
//                runOnUiThread {
                    fusedLocation = p0.lastLocation
                    val sendLocation = SendLocation()
                    sendLocation.location.add(fusedLocation!!.latitude)
                    sendLocation.location.add(fusedLocation!!.longitude)
                    Timber.i("LOCATION DRIVER:"+" "+fusedLocation!!.latitude+" "+fusedLocation!!.longitude)
                    homeViewModel.sendDriverLocation(fusedLocation!!)
                    homeViewModel.sendDriverLocationToRest(sendLocation)
//                    checkFastLocUpdate(homeViewModel.currentTripLiveData.value)
//                }

            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
        }

    }



}