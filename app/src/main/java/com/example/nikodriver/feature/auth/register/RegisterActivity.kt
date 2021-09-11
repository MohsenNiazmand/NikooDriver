package com.example.nikodriver.feature.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nikodriver.R
import com.example.nikodriver.feature.auth.upload_docs.UploadDocsActivity
import com.example.nikodriver.feature.home.HomeActivity
import kotlinx.android.synthetic.main.activity_register.*



class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerBtn.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, UploadDocsActivity::class.java))

        }





    }
}
