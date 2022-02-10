package com.akaf.nikoodriver.feature.main.offersHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.App
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.offersHistoryResponse.OffersHistoryDoc
import com.akaf.nikoodriver.feature.main.transactions.UTC_TIME_FORMATT
import com.github.eloyzone.jalalicalendar.DateConverter
import com.github.eloyzone.jalalicalendar.JalaliDateFormatter
import kotlinx.android.synthetic.main.item_offers_history.view.*
import java.text.SimpleDateFormat
import java.util.*

class OffersHistoryAdapter : RecyclerView.Adapter<OffersHistoryAdapter.OffersHistoryViewHolder>() {


    inner class OffersHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

        private val differCallback = object : DiffUtil.ItemCallback<OffersHistoryDoc>() {
            override fun areItemsTheSame(oldItem: OffersHistoryDoc, newItem: OffersHistoryDoc): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: OffersHistoryDoc, newItem: OffersHistoryDoc): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, differCallback)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersHistoryViewHolder {
        return OffersHistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_offers_history,parent,false))
    }

    override fun onBindViewHolder(holder: OffersHistoryViewHolder, position: Int) {
        val doc=differ.currentList[position]
        holder.itemView.apply {
            originHisTv.text=doc.sourceCity
            destinationHisTv.text=doc.destinationCity
            tripCostHisTv.text=doc.cost.toString()
            passengersCountHisTv.text=doc.TotalPassangers.toString()
            serviceTypeHisTv.text=doc.description
            offersHistoryIdTv.text=doc.id.toString()


            when(doc.status){
                "driver_canceled"->{tripStatusHisTv.text=App.context.resources.getText(R.string.driverCanceledTrip)}
                "user_canceled"->{tripStatusHisTv.text=App.context.resources.getText(R.string.userCanceledTrip)}
                "trip_ended"->{tripStatusHisTv.text=App.context.resources.getText(R.string.tripEnded)}
            }

            if (doc.startAt!=null){
                val date=doc.startAt
                val dateConverter =  DateConverter();
                val readDate = SimpleDateFormat(UTC_TIME_FORMATT)
                readDate.timeZone= TimeZone.getTimeZone("UTC")
                val correctDate =readDate.parse(date)
                val ourTimeFormat = SimpleDateFormat("MM/dd/yyyy'T'HH:mm")
                val correctDateFormatted=ourTimeFormat.format(correctDate!!)
                // Convert Gregorian date to Jalali
                val result = correctDateFormatted.format( JalaliDateFormatter("yyyy/mm/dd", JalaliDateFormatter.FORMAT_IN_PERSIAN))
                val jalaliDate=dateConverter.gregorianToJalali(result.substring(6,10).toInt(),result.substring(0,2).toInt(),result.substring(3,5).toInt())
                serviceDateHisTv.text= jalaliDate.toString() + " "+ "ساعت" +" "+ result.substring(11)
            }



        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}