

package com.example.nikodriver.feature.auth.verification


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nikodriver.R
import com.example.nikodriver.common.NikoSingleObserver
import com.example.nikodriver.data.verificationResponse.VerificationResponse
import com.example.nikodriver.feature.auth.register.RegisterActivity
import com.example.nikodriver.feature.auth.upload_docs.UploadDocsActivity
import com.example.nikodriver.feature.home.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_verification.*
import org.koin.android.viewmodel.ext.android.viewModel

class VerificationActivity : AppCompatActivity() {
    val viewModel: VerificationViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        val mobileNum = intent!!.getStringExtra("MOBILE_NUM")

        nextBtn.setOnClickListener {


            if (mobileNum != null) {

                viewModel.verification(mobileNum, verificationCodeEt.text.toString())
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : NikoSingleObserver<VerificationResponse>(compositeDisposable) {
                        override fun onSuccess(t: VerificationResponse) {
                            val status = t.data.driver.status
                            when (status) {
                                "active" ->
                                    startActivity(
                                    Intent(
                                        this@VerificationActivity,
                                        HomeActivity::class.java
                                    )
                                )
                                "fillInfo" ->
                                    startActivity(
                                    Intent(
                                        this@VerificationActivity,
                                        RegisterActivity::class.java
                                    )
                                )
                                "insufficient_docs" ->
                                    startActivity(
                                    Intent(
                                        this@VerificationActivity,
                                        UploadDocsActivity::class.java
                                    )
                                )

                                "blocked" ->
                                    runOnUiThread {
                                    kotlin.run {
                                        Toast.makeText(
                                            applicationContext,
                                            "اکانت شما مسدود شده است",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }

                                }
                                "company_deactivated" ->
                                    runOnUiThread {
                                    kotlin.run {
                                        Toast.makeText(
                                            applicationContext,
                                            "از سمت شرکت مسدود شدید",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }

                                }
                                else ->
                                    runOnUiThread {
                                        kotlin.run {
                                            Toast.makeText(
                                                applicationContext,
                                                "با پشتیبانی تماس بگیرید",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }

                                    }
                            }
                        }
                    })


            }
        }
    }
}