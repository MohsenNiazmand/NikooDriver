package com.akaf.nikoodriver.feature.main.current_trips

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.akaf.nikoodriver.R
import kotlinx.android.synthetic.main.dialog_cancel_trip.*
import kotlinx.android.synthetic.main.dialog_cancel_trip.view.*
import org.koin.android.ext.android.inject

class CancelTripDialog: DialogFragment() {
    val currentTripsViewModel:CurrentTripsViewModel by inject()
    var serviceId:Int=0
    var location0:Double=0.0
    var location1:Double=0.0
    var location: ArrayList<Double> = ArrayList()

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        serviceId= arguments?.getInt("serviceId",0)!!
        location0= arguments?.getDouble("loc0",0.0)!!
        location1= arguments?.getDouble("loc1",0.0)!!
        location.add(location0)
        location.add(location1)

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }

    }

    @SuppressLint("BinaryOperationInTimber")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_cancel_trip, container, false)

        view.wrongTimingBt.setOnClickListener {
            dismiss()
            currentTripsViewModel.cancelTrip(serviceId,wrongTimingBt.text.toString(),location)
            currentTripsViewModel.currentTrips()
        }
        view.wrongNavigatingBt.setOnClickListener {
            dismiss()
            currentTripsViewModel.cancelTrip(serviceId,wrongNavigatingBt.text.toString(),location)
            currentTripsViewModel.currentTrips()
        }
        view.declinePassengerLocBt.setOnClickListener {
            dismiss()
            currentTripsViewModel.cancelTrip(serviceId,declinePassengerLocBt.text.toString(),location)
            currentTripsViewModel.currentTrips()
        }
        view.passengerWasNotAtLocBt.setOnClickListener {
            dismiss()
            currentTripsViewModel.cancelTrip(serviceId,passengerWasNotAtLocBt.text.toString(),location)
            currentTripsViewModel.currentTrips()
        }
        view.otherReasonBt.setOnClickListener {
            dismiss()
            currentTripsViewModel.cancelTrip(serviceId,otherReasonBt.text.toString(),location)
            currentTripsViewModel.currentTrips()
        }

        return view
    }

}