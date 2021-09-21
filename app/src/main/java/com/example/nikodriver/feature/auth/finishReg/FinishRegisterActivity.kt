package com.example.nikodriver.feature.auth.finishReg

import android.content.Intent
import android.os.Bundle
import com.example.nikodriver.R
import com.example.nikodriver.common.BaseActivity
import com.example.nikodriver.feature.auth.login.LoginActivity
import kotlinx.android.synthetic.main.activity_finish_register.*

class FinishRegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_register)


        proceedRegistration.setOnClickListener {
            startActivity(Intent(this@FinishRegisterActivity, LoginActivity::class.java))

        }
    }

    override fun onBackPressed() {
    }

}