package com.akaf.nikoodriver.feature.home.credit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.feature.home.HomeViewModel
import kotlinx.android.synthetic.main.dialog_credit.view.*
import org.koin.android.ext.android.inject

class CreditDialog:DialogFragment() {
    val homeViewModel:HomeViewModel by inject()
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)

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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_credit, container, false)
        view.closeDialogBtn.setOnClickListener { dismiss() }
        view.totalTripsTv.text=homeViewModel.totalTrips
        view.totalDistanceTv.text=homeViewModel.totalDistance
        view.totalTimeTv.text=homeViewModel.totalTime
        view.rateTTv.text=homeViewModel.rate


        return view


    }

}