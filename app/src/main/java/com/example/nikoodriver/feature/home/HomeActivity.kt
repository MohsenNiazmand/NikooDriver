

package com.example.nikoodriver.feature.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.example.nikoodriver.R
import com.example.nikoodriver.common.BaseActivity
import com.example.nikoodriver.feature.auth.login.LoginActivity
import com.example.nikoodriver.feature.home.credit.CreditDialog
import com.example.nikoodriver.feature.current_travel.CurrentTravelActivity
import com.example.nikoodriver.feature.declined_passengers.DeclinedPassengersActivity
import com.example.nikoodriver.feature.travel_registeration.TravelRegistrationActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject

class HomeActivity : BaseActivity() {
    val sharedPreferences: SharedPreferences by inject()

    override fun onStart() {
        super.onStart()
        //we check tokenExistence ,if it exist user goes to home
        val tokenExistence=sharedPreferences.getString("access_token", null)
        if (tokenExistence==null){
            finish()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            overridePendingTransition(0, 0);
        }
    }

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (!CheckInternet()){
            val snackBar = Snackbar
                .make(
                    findViewById(R.id.homeRoot),
                    "اینترنت متصل نیست",
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction("بررسی مجدد ...") { view: View? -> finish();
                    startActivity(intent); }
            snackBar.show()
        }

        if (!CheckGps()){
            val snackBar = Snackbar
                .make(
                    findViewById(R.id.homeRoot),
                    "مکان یاب خود را روشن نمایید",
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction("بررسی مجدد ...") { view: View? -> finish();
                    startActivity(intent); }
            snackBar.show()
        }


        activeBtn.setOnClickListener {
            activationTv.visibility= View.GONE
            activeBtn.visibility= View.GONE
            deActiveBtn.visibility=View.VISIBLE

        }

        deActiveBtn.setOnClickListener {
            activationTv.visibility= View.VISIBLE
            activeBtn.visibility= View.VISIBLE
            deActiveBtn.visibility=View.GONE

        }

        creditBtn.setOnClickListener {
            val creditDialog = CreditDialog()
            creditDialog.show(supportFragmentManager, null)

        }

        travelRegistrationBtn.setOnClickListener {
            startActivity(Intent(this@HomeActivity, TravelRegistrationActivity::class.java))

        }

        declinedPassengersBtn.setOnClickListener {
            startActivity(Intent(this@HomeActivity, DeclinedPassengersActivity::class.java))

        }

        currentTravelBtn.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CurrentTravelActivity::class.java))

        }


    }


}