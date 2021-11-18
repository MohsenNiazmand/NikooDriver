package com.akaf.nikoodriver.feature.current_trips

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.location.SendLocation
import kotlinx.android.synthetic.main.dialog_cancel_trip.*
import kotlinx.android.synthetic.main.dialog_cancel_trip.view.*
import org.koin.android.ext.android.inject
import timber.log.Timber

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
        }
        view.wrongNavigatingBt.setOnClickListener {
            dismiss()
            currentTripsViewModel.cancelTrip(serviceId,wrongNavigatingBt.text.toString(),location)
        }
        view.declinePassengerLocBt.setOnClickListener {
            dismiss()
            currentTripsViewModel.cancelTrip(serviceId,declinePassengerLocBt.text.toString(),location)
        }
        view.passengerWasNotAtLocBt.setOnClickListener {
            dismiss()
            currentTripsViewModel.cancelTrip(serviceId,passengerWasNotAtLocBt.text.toString(),location)
        }
        view.otherReasonBt.setOnClickListener {
            dismiss()
            currentTripsViewModel.cancelTrip(serviceId,otherReasonBt.text.toString(),location)
        }

        return view
    }

}