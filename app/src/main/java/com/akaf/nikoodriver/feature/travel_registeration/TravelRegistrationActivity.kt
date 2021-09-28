package com.akaf.nikoodriver.feature.travel_registeration

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.akaf.nikoodriver.R
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlinx.android.synthetic.main.activity_travel_registration.*


class TravelRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_registration)



    }

     fun datePickerA(v: View){
        val picker = PersianDatePickerDialog(this)
            .setPositiveButtonString("باشه")
            .setNegativeButton("بیخیال")
            .setTodayButton("امروز")
            .setTodayButtonVisible(true)
            .setMinYear(1300)
            .setAllButtonsTextSize(16)
            .setPickerBackgroundColor(Color.WHITE)
            .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
            .setInitDate(1374, 2, 1)
            .setActionTextColor(Color.BLACK)
            .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
            .setShowInBottomSheet(true)
            .setListener(object : PersianPickerListener{
                override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                    val travelDateTxt =
                        persianPickerDate.persianYear.toString() + "/" + persianPickerDate.persianMonth + "/" + persianPickerDate.persianDay
                    travelDateEt.setText(travelDateTxt)




                }


                override fun onDismissed() {


                }

            })
        picker.show()
    }
}