package com.akaf.nikoodriver.feature.auth.registering

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.akaf.nikoodriver.R

class AuthActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        val token=intent!!.getStringExtra("token")
        if (token != null) {
            authViewModel.token=token
        }
    }
}