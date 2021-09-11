package com.example.nikodriver.feature.auth.upload_docs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nikodriver.R
import com.example.nikodriver.feature.home.HomeActivity
import kotlinx.android.synthetic.main.activity_upload_docs.*

class UploadDocsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_docs)

        proceedDocsBtn.setOnClickListener {
            startActivity(Intent(this@UploadDocsActivity, HomeActivity::class.java))

        }
    }
}