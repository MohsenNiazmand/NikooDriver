

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
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_verification.*
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import android.text.TextUtils
import com.example.nikodriver.common.BaseActivity

import com.google.gson.Gson
import retrofit2.Response
import java.lang.ProcessBuilder.Redirect.to


class VerificationActivity : BaseActivity() {
    val viewModel: VerificationViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        val mobileNum = intent!!.getStringExtra("MOBILE_NUM")

        nextBtn.setOnClickListener {


            if (mobileNum != null && verificationCodeEt.text.toString().isNotEmpty()) {

                viewModel.verification(mobileNum, verificationCodeEt.text.toString())
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(object :
                        NikoSingleObserver<Response<VerificationResponse>>(compositeDisposable) {
                        override fun onSuccess(t: Response<VerificationResponse>) {

                            val response = t.body()?.data


                            if (response != null) {
                                when (response.driver.status) {
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

                            } else {

                                runOnUiThread {
                                    kotlin.run {
                                        Toast.makeText(
                                            applicationContext,
                                            "کد وارد شده صحیح نمی باشد",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }

                                }


                            }

                        }
                    })


            }
        }


        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()

    }
}