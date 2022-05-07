package com.akaf.nikoodriver.feature.main.transactions

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.App
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.transactionsResponse.TransactionsData
import com.github.eloyzone.jalalicalendar.DateConverter
import com.github.eloyzone.jalalicalendar.JalaliDateFormatter
import java.text.SimpleDateFormat
import java.util.*


const val PATTERN_JALALI_DATE = "yyyy/MM/dd"
const val PATTERN_JALALI_DATE_AND_TIME = "yyyy/MM/dd HH:mm"
const val UTC_TIME_FORMATT = "yyyy-MM-dd'T'HH:mm"

class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>() {

    var transactions=ArrayList<TransactionsData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()

        }


    inner class TransactionsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val transactionAmountTv=itemView.findViewById<TextView>(R.id.transactionAmountTv)
        val transactionCodeTv=itemView.findViewById<TextView>(R.id.transactionCodeTv)
        val transactionReasonTv=itemView.findViewById<TextView>(R.id.transactionReasonTv)
        val transactionTypeTv=itemView.findViewById<TextView>(R.id.transactionTypeTv)
        val transactionDateTv=itemView.findViewById<TextView>(R.id.transactionDateTv)

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(transaction:TransactionsData){
            transactionAmountTv.text= (transaction.amount.toDouble().toInt()/10).toString()
            transactionCodeTv.text=transaction.id.toString()
            transactionReasonTv.text=transaction.reason

            when (transaction.type){
                "inapp.CancelDriver"->{
                    transactionTypeTv.text= App.context.resources.getText(R.string.driverCanceledTrip)

                }
                 "inapp.PayTrip"->{
                     transactionTypeTv.text=App.context.resources.getText(R.string.inAppPay)

                 }

            }

            val date=transaction.createdAt
            val dateConverter =  DateConverter();
            val readDate = SimpleDateFormat(UTC_TIME_FORMATT)
            readDate.timeZone= TimeZone.getTimeZone("UTC")
            val correctDate =readDate.parse(date!!)
            val ourTimeFormat = SimpleDateFormat("MM/dd/yyyy'T'HH:mm")
            val correctDateFormatted=ourTimeFormat.format(correctDate!!)
            // Convert Gregorian date to Jalali
            val result = correctDateFormatted.format( JalaliDateFormatter("yyyy/mm/dd", JalaliDateFormatter.FORMAT_IN_PERSIAN))
            val jalaliDate=dateConverter.gregorianToJalali(result.substring(6,10).toInt(),result.substring(0,2).toInt(),result.substring(3,5).toInt())
            transactionDateTv.text= jalaliDate.toString() + " "+ "ساعت" +" "+ result.substring(11)
        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        return TransactionsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int {
      return transactions.size
    }
}