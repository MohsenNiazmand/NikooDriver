package com.akaf.nikoodriver.feature.current_trips

import android.annotation.SuppressLint
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

            if (it.data?.isEmpty() == true){
                ivEmptyC.visibility=View.VISIBLE
            }else{
                ivEmptyC.visibility=View.GONE
                currentTripsAdapter.currentTripCallback=this
                if (it.data!=null)
                    currentTripsAdapter.currentTrips= it.data as ArrayList<CurrentTripsData>
            }


        }




        currentTripsViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

    }

//    override fun onResume() {
//        super.onResume()
//        if (currentTripsAdapter.itemCount==0){
//            ivEmptyC.visibility=View.VISIBLE
//        }
//    }




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

            currentTripsViewModel.pickUp(currentTrip.id,currentTrip.Source.id,30.54687,57.26498)

    }

    override fun onEndTripClicked(currentTrip: CurrentTripsData) {
        currentTripsViewModel.dropOf(currentTrip.id,currentTrip.Source.id,30.54687,57.26498)
        currentTripsViewModel.completeTrip(currentTrip.id)
    }

    override fun onCancelBtnClicked(currentTrip: CurrentTripsData) {
        currentTripsViewModel.cancelTrip(currentTrip.id)
    }
}