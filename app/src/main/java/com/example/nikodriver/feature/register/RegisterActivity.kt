package com.example.nikodriver.feature.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.nikodriver.R
import com.example.nikodriver.feature.home.HomeActivity
import com.example.nikodriver.feature.verification.VerificationActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val alphabet = resources.getStringArray(R.array.Alphabet)

        registerBtn.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))

        }

    }
}
