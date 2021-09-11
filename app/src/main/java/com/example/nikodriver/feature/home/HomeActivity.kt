

package com.example.nikodriver.feature.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.nikodriver.R
import com.example.nikodriver.feature.credit.CreditDialog
import com.example.nikodriver.feature.current_travel.CurrentTravelActivity
import com.example.nikodriver.feature.declined_passengers.DeclinedPassengersActivity
import com.example.nikodriver.feature.travel_registeration.TravelRegistrationActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


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