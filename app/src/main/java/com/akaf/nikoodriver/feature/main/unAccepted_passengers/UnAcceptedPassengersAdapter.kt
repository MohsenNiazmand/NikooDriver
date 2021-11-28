package com.akaf.nikoodriver.feature.main.unAccepted_passengers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.unAcceptedPassengers.UnAcceptedPassengersData
import com.akaf.nikoodriver.feature.main.transactions.UTC_TIME_FORMATT
import com.github.eloyzone.jalalicalendar.DateConverter
import com.github.eloyzone.jalalicalendar.JalaliDateFormatter
import kotlinx.android.synthetic.main.item_declined_passenger.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UnAcceptedPassengersAdapter :
    RecyclerView.Adapter<UnAcceptedPassengersAdapter.UnAcceptedPassengersViewHolder>() {
    var unAcceptedPassengersCallBacks: UnAcceptedPassengersCallBacks?=null

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


        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(passenger:UnAcceptedPassengersData){
            originTv.text=passenger.sourceCity
            destinationTv.text=passenger.destinationCity
            fareTv.text=passenger.cost
//            serviceDateTv.text=passenger.startAt
            val date=passenger.startAt
            val dateConverter =  DateConverter();
            val readDate = SimpleDateFormat(UTC_TIME_FORMATT)
            readDate.timeZone= TimeZone.getTimeZone("UTC")
            val correctDate =readDate.parse(date)
            val ourTimeFormat = SimpleDateFormat("MM/dd/yyyy'T'HH:mm")
            val correctDateFormatted=ourTimeFormat.format(correctDate!!)
            // Convert Gregorian date to Jalali
            val result = correctDateFormatted.format( JalaliDateFormatter("yyyy/mm/dd", JalaliDateFormatter.FORMAT_IN_PERSIAN))
            val jalaliDate=dateConverter.gregorianToJalali(result.substring(6,10).toInt(),result.substring(0,2).toInt(),result.substring(3,5).toInt())
            serviceDateTv.text= jalaliDate.toString()
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