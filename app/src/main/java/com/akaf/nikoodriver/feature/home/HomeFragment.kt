package com.akaf.nikoodriver.feature.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.data.responses.location.SendLocation
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
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomeFragment : BaseFragment() {
    private val hiveMqttManager: HiveMqttManager by inject()
    val homeViewModel: HomeViewModel by inject()
    val sharedPreferences:SharedPreferences by inject()
    private var fusedLocation: Location? = null
    var isFastLocation = false
    lateinit var fusedLocationClient: FusedLocationProviderClient

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onlineStatus=sharedPreferences.getBoolean("isOnline",false)
        if (onlineStatus){
            active()
        }else
            deActive()


        homeViewModel.mqttState.observe(viewLifecycleOwner) {
            Log.d("TAG", "initConnectionState: " + it)
            if (it) {
                checkPermStartLocationUpdate()
//                checkNewTrip()
                connectedSign.visibility=View.VISIBLE
                disconnectSign.visibility=View.GONE
            }
            else {
                connectedSign.visibility=View.GONE
                disconnectSign.visibility=View.VISIBLE
            }
        }

        activeBtn.setOnClickListener {
            showEmptySeatsDialog()

        }

        deActiveBtn.setOnClickListener {
            deActive()

        }

        creditBtn.setOnClickListener {
            val creditDialog = CreditDialog()
           creditDialog.show(childFragmentManager, null)

        }

        travelRegistrationBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_travelRegistrationFragment)

        }

        declinedPassengersBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_declinedPassengersFragment)

        }

        currentTravelBtn.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_currentTravelFragment)

        }

        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }


    }


    private fun active(){
        activationTv.visibility= View.GONE
        activeBtn.visibility= View.GONE
        deActiveBtn.visibility=View.VISIBLE
        homeViewModel.setOnlineStatus(true)
        checkPermStartLocationUpdate()
    }

    private fun deActive(){
        activationTv.visibility= View.VISIBLE
        activeBtn.visibility= View.VISIBLE
        deActiveBtn.visibility=View.GONE
        homeViewModel.setOnlineStatus(false)
        DriverForegroundService.stopService(requireContext())
        stopLocationUpdates()
    }


    private fun showEmptySeatsDialog() {
        val emptySeatsView = layoutInflater.inflate(R.layout.dialog_empty_seats, null, false)
        val emptySeatsDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        emptySeatsDialog.setView(emptySeatsView)
        emptySeatsDialog.setCancelable(false)
        val emptySeatsEt= emptySeatsView.findViewById<EditText>(R.id.emptySeatsEt)
        emptySeatsView.findViewById<MaterialButton>(R.id.proceedEmptySeatsBtn).setOnClickListener {
            if (emptySeatsEt.text.isNotEmpty()){
                homeViewModel.setEmptySeats(emptySeatsEt.text.toString().toInt())
                emptySeatsDialog.dismiss()
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
                    Timber.i("LOCATION11"+fusedLocation!!.latitude+" "+fusedLocation!!.longitude)
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