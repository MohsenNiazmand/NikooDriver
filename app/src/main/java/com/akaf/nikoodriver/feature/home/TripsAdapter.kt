package com.akaf.nikoodriver.feature.home

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.mqttTripResponse.TripData
import kotlinx.android.synthetic.main.item_trip.view.*
import timber.log.Timber

class TripsAdapter() : RecyclerView.Adapter<TripsAdapter.TripsViewHolder>() {
    var cartItemViewCallBacks: CartItemViewCallBacks?=null

    var trips=ArrayList<TripData>()
    set(value) {
        field = value
        notifyDataSetChanged()

    }

    inner class TripsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        val travelDistanceTv=itemView.findViewById<TextView>(R.id.travelDistanceTv)
        val offerCostTv=itemView.findViewById<TextView>(R.id.offerCostTv)
        val offerOrigin=itemView.findViewById<TextView>(R.id.offerOrigin)
        val offerDestination=itemView.findViewById<TextView>(R.id.offerDestination)
        val deadlineForReceivingTravelTv=itemView.findViewById<TextView>(R.id.deadlineForReceivingTravelTv)
        val moreTripsPart=itemView.findViewById<RelativeLayout>(R.id.moreTripsPart)


        @SuppressLint("SetTextI18n")
        fun bind(trip: TripData){

            val timer = object: CountDownTimer(120000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    var diff = millisUntilFinished
                    val secondsInMilli: Long = 1000
                    val minutesInMilli = secondsInMilli * 60
                    val elapsedMinutes = diff / minutesInMilli
                    diff %= minutesInMilli
                    val elapsedSeconds = diff / secondsInMilli
              deadlineForReceivingTravelTv.text="$elapsedMinutes:$elapsedSeconds"
                }

                override fun onFinish() {
                    cartItemViewCallBacks?.timerFinished(trip)
//                    newOfferView.visibility=View.GONE
                }

            }

            fun startTimer(){
                timer.start()
            }

            fun cancelTimer(){
                timer.cancel()
            }
            trips.forEach {
                startTimer()
            }
            travelDistanceTv.text=trip.options.distance.toString()+" "+"کیلومتر"
            offerCostTv.text=trip.cost
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

//    fun addTrip(tripData: TripData){
//        val index=trips.indexOf(tripData)
//        if (index>-1){
//            trips.add(index,tripData)
//            notifyItemInserted(index)
//        }
//
//    }
}