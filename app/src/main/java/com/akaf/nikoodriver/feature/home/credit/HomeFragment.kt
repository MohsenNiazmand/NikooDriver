package com.akaf.nikoodriver.feature.home.credit

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.data.location.SendLocation
import com.akaf.nikoodriver.feature.home.HomeViewModel
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
    private var fusedLocation: Location? = null
    var isFastLocation = false
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

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





        homeViewModel.mqttState.observe(viewLifecycleOwner) {
            Log.d("TAG", "initConnectionState: " + it)
            if (it) {
                checkPermStartLocationUpdate()
//                checkNewTrip()
            } else {
                if (homeViewModel.onlineStatusLiveData.value == true) {
//                    txtConnectingToTheServer.visibility = View.VISIBLE
                } else {
//                    txtConnectingToTheServer.visibility = View.GONE

                }
            }
        }

        activeBtn.setOnClickListener {
            showEmptySeatsDialog()
//            activationTv.visibility= View.GONE
//            activeBtn.visibility= View.GONE
//            deActiveBtn.visibility=View.VISIBLE
//            hiveMqttManager.connect()
//            checkPermStartLocationUpdate()

        }

        deActiveBtn.setOnClickListener {
            activationTv.visibility= View.VISIBLE
            activeBtn.visibility= View.VISIBLE
            deActiveBtn.visibility=View.GONE
            hiveMqttManager.disconnect()
            stopLocationUpdates()

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


    }

    private fun showEmptySeatsDialog() {
        val emptySeatsView = layoutInflater.inflate(R.layout.dialog_empty_seats, null, false)
        val emptySeatsDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        emptySeatsDialog.setView(emptySeatsView)
        emptySeatsDialog.setCancelable(false)
        emptySeatsView.findViewById<MaterialButton>(R.id.proceedEmptySeatsBtn).setOnClickListener {
            emptySeatsDialog.dismiss()
            activationTv.visibility= View.GONE
            activeBtn.visibility= View.GONE
            deActiveBtn.visibility=View.VISIBLE
            hiveMqttManager.connect()
            checkPermStartLocationUpdate()
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
                    sendLocation.location.add(fusedLocation!!.longitude)
                    sendLocation.location.add(fusedLocation!!.latitude)
                    Timber.i("LOCATION11"+fusedLocation!!.longitude)
                    homeViewModel.sendDriverLocation(fusedLocation!!)
//                    homeViewModel.sendDriverLocationToRest(sendLocation)
//                    checkFastLocUpdate(homeViewModel.currentTripLiveData.value)
//                }

            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
        }
    }



}