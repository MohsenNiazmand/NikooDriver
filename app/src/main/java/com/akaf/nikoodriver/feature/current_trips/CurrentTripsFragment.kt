package com.akaf.nikoodriver.feature.current_trips

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsData
import kotlinx.android.synthetic.main.fragment_current_travel.*
import kotlinx.android.synthetic.main.fragment_declined_passengers.*
import org.koin.android.ext.android.inject

class CurrentTripsFragment : BaseFragment(),CurrentTripsAdapter.CurrentTripCallback {
    val currentTripsViewModel:CurrentTripsViewModel by inject()
    val currentTripsAdapter=CurrentTripsAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_travel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvCurrentTrips.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rvCurrentTrips.adapter=currentTripsAdapter
        currentTripsViewModel.currentTripsLiveData.observe(viewLifecycleOwner){

            currentTripsAdapter.currentTripCallback=this
            currentTripsAdapter.currentTrips= it.data as ArrayList<CurrentTripsData>

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
        currentTripsViewModel.pickUp(currentTrip.id,currentTrip.Source.id)
    }

    override fun onEndTripClicked(currentTrip: CurrentTripsData) {
        currentTripsViewModel.dropOf(currentTrip.id,currentTrip.Source.id)
        currentTripsViewModel.completeTrip(currentTrip.id)
    }
}