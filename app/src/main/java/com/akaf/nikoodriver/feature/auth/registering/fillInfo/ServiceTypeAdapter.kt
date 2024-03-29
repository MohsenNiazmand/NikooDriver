package com.akaf.nikoodriver.feature.auth.registering.fillInfo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.data.responses.serviceTypeResponse.Doc
import com.facebook.drawee.view.SimpleDraweeView
import timber.log.Timber

class ServiceTypeAdapter() :RecyclerView.Adapter<ServiceTypeAdapter.ServiceTypeViewHolder>() {
    var serviceTypeCallback: ServiceTypeCallback?=null

    var serviceTypes=ArrayList<Doc>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class ServiceTypeViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val typeName=itemView.findViewById<TextView>(R.id.typeName)
        val capacityTv=itemView.findViewById<TextView>(R.id.capacityTv)
        val facilityTv=itemView.findViewById<TextView>(R.id.facilityTv)
        val serviceTypeImg=itemView.findViewById<SimpleDraweeView>(R.id.serviceTypeImg)
        val serviceTypeProgImg=itemView.findViewById<ProgressBar>(R.id.serviceTypeProgImg)

        @SuppressLint("BinaryOperationInTimber")
        fun bind(serviceType: Doc){
            typeName.text=serviceType.name
            capacityTv.text=serviceType.capacity.toString()
            facilityTv.text=serviceType.description
            serviceTypeImg.setImageURI(serviceType.photoUrl)

            itemView.setOnClickListener {
                serviceTypeCallback?.onItemClicked(serviceType)
            }
        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceTypeViewHolder {
        return ServiceTypeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_service_type,parent,false)
        )    }

    override fun onBindViewHolder(holder: ServiceTypeViewHolder, position: Int) {
        holder.bind(serviceTypes[position])
    }

    override fun getItemCount(): Int = serviceTypes.size


    interface ServiceTypeCallback {
        fun onItemClicked(doc: Doc)

    }

}