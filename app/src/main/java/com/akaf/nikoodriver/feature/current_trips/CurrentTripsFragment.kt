package com.akaf.nikoodriver.feature.current_trips

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsData
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.fragment_current_travel.*
import kotlinx.android.synthetic.main.fragment_declined_passengers.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class CurrentTripsFragment : BaseFragment(),CurrentTripsAdapter.CurrentTripCallback {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkPermStartLocationUpdate()
        rvCurrentTrips.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rvCurrentTrips.adapter=currentTripsAdapter
        currentTripsViewModel.currentTripsLiveData.observe(viewLifecycleOwner){
        currentTripsAdapter.currentTripCallback=this
            if (it.data!=null)
                currentTripsAdapter.currentTrips= it.data as ArrayList<CurrentTripsData>
            when(it.data?.size){
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




    override fun onCallToPassengerClicked(currentTrip: CurrentTripsData) {
        Toast.makeText(context,currentTrip.phoneNumber.toString(),Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${currentTrip.phoneNumber}")
        }
        if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)
        }
    }

    override fun onStartTripClicked(currentTrip: CurrentTripsData) {
        currentTripsViewModel.startTrip(currentTrip.id)
    }

    override fun onIRodeClicked(currentTrip: CurrentTripsData) {

        latitude?.let {
            longitude?.let { it1 ->
                currentTripsViewModel.pickUp(currentTrip.id,currentTrip.Source.id,
                    it, it1
                )
            }
        }

    }

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
        currentTripsViewModel.cancelTrip(currentTrip.id)

            }
}