package com.akaf.nikoodriver.feature.travel_registeration

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.navigation.Navigation
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlinx.android.synthetic.main.fragment_travel_registration.view.*




class TravelRegistrationFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travel_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val travelDateEt=view.findViewById<TextView>(com.akaf.nikoodriver.R.id.travelDateEt)
        travelDateEt.setOnClickListener {
            datePickerA(view)
        }

    }

    fun datePickerA(v: View){
        val picker = PersianDatePickerDialog(context)
            .setPositiveButtonString("باشه")
            .setNegativeButton("بیخیال")
            .setTodayButton("امروز")
            .setTodayButtonVisible(true)
            .setMinYear(1398)
            .setMaxYear(1450)
            .setAllButtonsTextSize(16)
            .setPickerBackgroundColor(Color.WHITE)
            .setInitDate(1374, 2, 1)
            .setActionTextColor(Color.BLACK)
            .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
            .setShowInBottomSheet(true)
            .setListener(object : PersianPickerListener{
                override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                    val travelDateTxt =
                        persianPickerDate.persianYear.toString() + "/" + persianPickerDate.persianMonth + "/" + persianPickerDate.persianDay
                    view?.travelDateEt?.text=travelDateTxt




                }


                override fun onDismissed() {


                }

            })
        picker.show()
    }




}