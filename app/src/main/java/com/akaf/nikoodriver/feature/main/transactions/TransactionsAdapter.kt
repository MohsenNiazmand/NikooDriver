package com.akaf.nikoodriver.feature.main.transactions

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.transactionsResponse.TransactionsData

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

        fun bind(transaction:TransactionsData){
            transactionAmountTv.text= (transaction.amount.toDouble().toInt()/10).toString()
            transactionCodeTv.text=transaction.id.toString()
            transactionReasonTv.text=transaction.reason
            transactionTypeTv.text=transaction.type
            transactionDateTv.text=transaction.createdAt

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