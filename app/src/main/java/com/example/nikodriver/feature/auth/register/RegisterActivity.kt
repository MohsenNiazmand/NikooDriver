package com.example.nikodriver.feature.auth.register

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nikodriver.R
import com.example.nikodriver.feature.auth.upload_docs.UploadDocsActivity
import com.example.nikodriver.feature.home.HomeActivity
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_register.*



class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val firstName=userNameEtReg.text.toString()
        val lastFamily=userFamilyEtReg.text.toString()
        val nationalCode=nationalCodeEtReg.text.toString()
        val mobileNum=mobileEtReg.text.toString()
        val certificateCode=certificateCodeEtReg.text.toString()
        val plaque=(R.string.iran).toString()+" "+firstPlaqueEtReg.text.toString()+" "+firstPlaqueEtReg.text.toString()+" "+plaqueSpinner.selectedItem.toString()+" "+thirdPlaqueNumEtReg.text.toString()
        var vehicleType=""
        toggleBtnVehicleType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (carTypeBtn.isChecked){
                vehicleType=(R.string.car).toString()
            }else{
                vehicleType=(R.string.bus).toString()
            }
        }
        val vehicleColor=vehicleColorEtReg.text.toString()

        registerBtn.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, UploadDocsActivity::class.java))

        }

        insuranceExpireEt.setOnFocusChangeListener { view, b ->
            runOnUiThread {
                kotlin.run {
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
                        .setListener(object : PersianPickerListener {
                            override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                                val issueDateTxt =
                                    persianPickerDate.persianYear.toString() + "/" + persianPickerDate.persianMonth + "/" + persianPickerDate.persianDay
                                insuranceExpireEt.setText(issueDateTxt)
                            }

                            override fun onDismissed() {
                                Toast.makeText(this@RegisterActivity, "Dismissed", Toast.LENGTH_SHORT).show()
                            }
                        })

                    picker.show()


                }

            }

        }




    }
}
