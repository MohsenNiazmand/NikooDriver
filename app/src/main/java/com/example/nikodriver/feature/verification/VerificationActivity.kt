

package com.example.nikodriver.feature.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nikodriver.R
import com.example.nikodriver.feature.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        nextBtn.setOnClickListener {
            startActivity(Intent(this@VerificationActivity, RegisterActivity::class.java))

        }
    }
}