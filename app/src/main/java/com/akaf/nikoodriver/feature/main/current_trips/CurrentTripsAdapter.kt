package com.akaf.nikoodriver.feature.main.current_trips

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.currentTripsResponse.CurrentTripsData
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.item_current_trips.view.*

class CurrentTripsAdapter : RecyclerView.Adapter<CurrentTripsAdapter.CurrentTripsViewHolder>() {
    var currentTripCallback:CurrentTripCallback?=null

    var currentTrips=ArrayList<CurrentTripsData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()

        }

    inner class CurrentTripsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tripCodeTv=itemView.findViewById<TextView>(R.id.tripCodeTv)
        val passengersCountTv=itemView.findViewById<TextView>(R.id.passengersCountTv)
        val firstPassengerName=itemView.findViewById<TextView>(R.id.firstPassengerName)
        val currentServiceDateTv=itemView.findViewById<TextView>(R.id.currentServiceDateTv)
        val callToPassengerCurrentBtn=itemView.findViewById<MaterialButton>(R.id.callToPassengerCurrentBtn)
        val iRodeBtn=itemView.findViewById<MaterialButton>(R.id.iRodeBtn)
        val endTripBtn=itemView.findViewById<MaterialButton>(R.id.endTripBtn)
        val startTripBtn=itemView.findViewById<MaterialButton>(R.id.startTripBtn)
        val cancelTripBtn=itemView.findViewById<MaterialButton>(R.id.cancelTripBtn)

        @SuppressLint("NotifyDataSetChanged")
        fun bind(currentTrip:CurrentTripsData){
            tripCodeTv.text=currentTrip.id.toString()
            passengersCountTv.text=currentTrip.TotalPassangers.toString()
            firstPassengerName.text=currentTrip.User.fullName
            currentServiceDateTv.text=currentTrip.createdAt
            itemView.callToPassengerCurrentBtn.setOnClickListener {
                currentTripCallback?.onCallToPassengerClicked(currentTrip)
            }

            itemView.startTripBtn.setOnClickListener {
                currentTripCallback?.onStartTripClicked(currentTrip)
                startTripBtn.visibility=View.GONE
                iRodeBtn.visibility=View.VISIBLE

            }
            itemView.iRodeBtn.setOnClickListener {
                currentTripCallback?.onIRodeClicked(currentTrip)
                iRodeBtn.visibility=View.GONE
                endTripBtn.visibility=View.VISIBLE
            }
            itemView.endTripBtn.setOnClickListener {
                currentTripCallback?.onEndTripClicked(currentTrip)
                removeTripFromList(currentTrip)
            }
            itemView.cancelTripBtn.setOnClickListener {
//                removeTripFromList(currentTrip)
                currentTripCallback?.onCancelBtnClicked(currentTrip)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentTripsViewHolder {
        return CurrentTripsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_current_trips,parent,false)
        )
    }

    override fun onBindViewHolder(holder: CurrentTripsViewHolder, position: Int) {
        holder.bind(currentTrips[position])
    }

    override fun getItemCount(): Int {
       return currentTrips.size
    }

    interface CurrentTripCallback{
        fun onCallToPassengerClicked(currentTrip: CurrentTripsData)
        fun onStartTripClicked(currentTrip: CurrentTripsData)
        fun onIRodeClicked(currentTrip: CurrentTripsData)
        fun onEndTripClicked(currentTrip: CurrentTripsData)
        fun onCancelBtnClicked(currentTrip: CurrentTripsData)

    }


    fun removeTripFromList(currentTrip: CurrentTripsData){
        val index = currentTrips.indexOf(currentTrip)
        if (index > -1) {
            currentTrips.removeAt(index)
            notifyItemRemoved(index)
        }

    }

}