package com.akaf.nikoodriver.feature.main.current_trips

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsData
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.fragment_current_travel.*
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinApiExtension

class CurrentTripsFragment : BaseFragment(),CurrentTripsAdapter.CurrentTripCallback,CancelTripDialog.TripCanceled {
    val currentTripsViewModel:CurrentTripsViewModel by inject()
    val currentTripsAdapter=CurrentTripsAdapter()
    val latitude: Double?
        get()=fusedLocation?.latitude
    val longitude :Double?
    get() = fusedLocation?.longitude
    val handler = Handler()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_travel, container, false)
    }

    @KoinApiExtension
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkPermStartLocationUpdate()
        rvCurrentTrips.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,true)
        rvCurrentTrips.adapter=currentTripsAdapter
        currentTripsViewModel.currentTripsLiveData.observe(viewLifecycleOwner){
        currentTripsAdapter.currentTripCallback=this
            if (it?.data!=null)
                currentTripsAdapter.currentTrips= it.data as ArrayList<CurrentTripsData>
            when(it?.data?.size){
                0->ivEmptyC.visibility=View.VISIBLE
                else->ivEmptyC.visibility=View.GONE
            }


        }

        currentTripsViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

    }


    override var locationCallback = object : LocationCallback() {
        @SuppressLint("BinaryOperationInTimber")
        override fun onLocationResult(p0: LocationResult?) {
            if (p0 != null) {
                fusedLocation = p0.lastLocation
            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
        }

    }




    @SuppressLint("QueryPermissionsNeeded")
    @KoinApiExtension
    override fun onCallToPassengerClicked(currentTrip: CurrentTripsData) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${currentTrip.phoneNumber}")
        }
        if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)

        }
    }

    @KoinApiExtension
    override fun onStartTripClicked(currentTrip: CurrentTripsData) {
        currentTripsViewModel.startTrip(currentTrip.id)
    }

    @KoinApiExtension
    override fun onpassengerPickedUpClicked(currentTrip: CurrentTripsData) {

        latitude?.let {
            longitude?.let { it1 ->
                currentTripsViewModel.pickUp(currentTrip.id,currentTrip.Source.id,
                    it, it1
                )
            }
        }

    }

    @KoinApiExtension
    override fun onEndTripClicked(currentTrip: CurrentTripsData) {
        latitude?.let {
            longitude?.let { it1 ->
                currentTripsViewModel.dropOf(currentTrip.id,currentTrip.Source.id,
                    it, it1
                )
            }
        }

        currentTripsViewModel.dropOfLiveData.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                currentTripsViewModel.completeTrip(currentTrip.id)
            }
        }



        handler.postDelayed({
            currentTripsViewModel.currentTrips()
        }, 1000)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCancelBtnClicked(currentTrip: CurrentTripsData) {
        val cancelTripDialog = CancelTripDialog()
        val bundle=Bundle()
        bundle.putInt("serviceId",currentTrip.id)
        latitude?.let { bundle.putDouble("loc0", it) }
        longitude?.let { bundle.putDouble("loc1", it) }
        cancelTripDialog.arguments=bundle
        cancelTripDialog.show(childFragmentManager, null)
        cancelTripDialog.tripCanceled=this
            }

    override fun tripCanceled() {
        handler.postDelayed({
            currentTripsViewModel.currentTrips()
        }, 1000)
    }
}