package com.akaf.nikoodriver.feature.main

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.mqttTripResponse.TripData
import kotlinx.android.synthetic.main.item_trip.view.*



class TripsAdapter() : RecyclerView.Adapter<TripsAdapter.TripsViewHolder>() {
    var cartItemViewCallBacks: CartItemViewCallBacks?=null

    var trips=ArrayList<TripData>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()

    }

    inner class TripsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val travelDistanceTv=itemView.findViewById<TextView>(R.id.travelDistanceTv)
        val offerCostTv=itemView.findViewById<TextView>(R.id.offerCostTv)
        val offerOrigin=itemView.findViewById<TextView>(R.id.offerOrigin)
        val offerDestination=itemView.findViewById<TextView>(R.id.offerDestination)
        @SuppressLint("SetTextI18n")
        fun bind(trip: TripData){

            val timer = object: CountDownTimer(60000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    removeTripFromList(trip)
                    cartItemViewCallBacks?.timerFinished(trip)
                }

            }.start()
              fun cancelTimer(){ timer.cancel() }



            travelDistanceTv.text=trip.options.distance.toString()+" "+"کیلومتر"
            offerCostTv.text=trip.cost+" "+"تومان"
            offerOrigin.text=trip.sourceCity
            offerDestination.text=trip.destinationCity
           itemView.rejectOfferBtn.setOnClickListener {

               if (trips.size>1){
                   removeTripFromList(trip)
                   cancelTimer()
                   cartItemViewCallBacks?.onRejectBtnClicked(trip)
               }
               if (trips.size==1){
                   removeTripFromList(trip)
                   cancelTimer()
                   cartItemViewCallBacks?.onRejectBtnClicked(trip)
               }
            }
            itemView.acceptOfferBtn.setOnClickListener {
                removeTripFromList(trip)
                cancelTimer()
                cartItemViewCallBacks?.onAcceptBtnClicked(trip)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripsViewHolder {
        return TripsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_trip,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TripsViewHolder, position: Int) {
        holder.bind(trips[position])

    }

    override fun getItemCount(): Int {
        return  trips.size
    }

    interface CartItemViewCallBacks{
        fun onRejectBtnClicked(tripData: TripData)
        fun onAcceptBtnClicked(tripData: TripData)
        fun timerFinished(tripData: TripData)
    }


    fun removeTripFromList(tripData: TripData){
        val index = trips.indexOf(tripData)
        if (index > -1) {
            trips.removeAt(index)
            notifyItemRemoved(index)
        }

    }

}