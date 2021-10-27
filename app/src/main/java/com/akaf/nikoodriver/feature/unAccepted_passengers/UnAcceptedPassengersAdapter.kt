package com.akaf.nikoodriver.feature.unAccepted_passengers

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.UnAcceptedPassengers.UnAcceptedPassengersData
import com.akaf.nikoodriver.data.responses.mqttTripResponse.TripData
import com.akaf.nikoodriver.feature.home.HomeViewModel
import kotlinx.android.synthetic.main.item_declined_passenger.view.*
import org.koin.java.KoinJavaComponent.inject
import kotlin.coroutines.coroutineContext

class UnAcceptedPassengersAdapter :
    RecyclerView.Adapter<UnAcceptedPassengersAdapter.UnAcceptedPassengersViewHolder>() {
    var unAcceptedPassengersCallBacks: UnAcceptedPassengersAdapter.UnAcceptedPassengersCallBacks?=null

    var passengers=ArrayList<UnAcceptedPassengersData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()

        }



    inner class UnAcceptedPassengersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val originTv=itemView.findViewById<TextView>(R.id.originTv)
        val destinationTv=itemView.findViewById<TextView>(R.id.destinationTv)
        val fareTv=itemView.findViewById<TextView>(R.id.fareTv)
        val serviceDateTv=itemView.findViewById<TextView>(R.id.serviceDateTv)


        fun bind(passenger:UnAcceptedPassengersData){
            originTv.text=passenger.sourceCity
            destinationTv.text=passenger.destinationCity
            fareTv.text=passenger.cost
            serviceDateTv.text=passenger.startAt
            itemView.acceptRequestBtn.setOnClickListener {
                if (passengers.size==1){
                    removePassengerFromList(passenger)
                    unAcceptedPassengersCallBacks?.onAcceptBtnClicked(passenger)
                }
                if (passengers.size>1){
                    removePassengerFromList(passenger)
                    unAcceptedPassengersCallBacks?.onAcceptBtnClicked(passenger)

                }

            }

            itemView.declineRequestBtn.setOnClickListener {
                if (passengers.size==1){
                    removePassengerFromList(passenger)
                    unAcceptedPassengersCallBacks?.onRejectBtnClicked(passenger)
                }
                if (passengers.size>1){
                    removePassengerFromList(passenger)
                    unAcceptedPassengersCallBacks?.onRejectBtnClicked(passenger)

                }

            }

            itemView.navigationBtn.setOnClickListener {
                unAcceptedPassengersCallBacks?.onNavigationClicked(passenger)
            }

            itemView.callToPassengerBtn.setOnClickListener {
                unAcceptedPassengersCallBacks?.onCallClicked(passenger)

            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UnAcceptedPassengersViewHolder {
        return UnAcceptedPassengersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_declined_passenger,parent,false)
        )
    }

    override fun onBindViewHolder(holder: UnAcceptedPassengersViewHolder, position: Int) = holder.bind(passengers[position])

    override fun getItemCount(): Int = passengers.size



    interface UnAcceptedPassengersCallBacks{
        fun onRejectBtnClicked(passenger: UnAcceptedPassengersData)
        fun onAcceptBtnClicked(passenger: UnAcceptedPassengersData)
        fun onNavigationClicked(passenger: UnAcceptedPassengersData)
        fun onCallClicked(passenger: UnAcceptedPassengersData)
    }

    fun removePassengerFromList(passenger: UnAcceptedPassengersData){
        val index = passengers.indexOf(passenger)
        if (index > -1) {
            passengers.removeAt(index)
            notifyItemRemoved(index)
        }

    }

}