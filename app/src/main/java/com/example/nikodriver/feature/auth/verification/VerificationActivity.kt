

package com.example.nikodriver.feature.auth.verification


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.nikodriver.R
import com.example.nikodriver.common.NikoSingleObserver
import com.example.nikodriver.data.verificationResponse.VerificationResponse
import com.example.nikodriver.feature.auth.fillInfo.FillInfoActivity
import com.example.nikodriver.feature.auth.upload_docs.UploadDocsActivity
import com.example.nikodriver.feature.home.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_verification.*
import org.koin.android.viewmodel.ext.android.viewModel
import com.example.nikodriver.common.BaseActivity
import com.google.android.material.snackbar.Snackbar

import retrofit2.Response


class VerificationActivity : BaseActivity() {
    val viewModel: VerificationViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        val mobileNum = intent!!.getStringExtra("MOBILE_NUM")


        if (!CheckInternet()){

            val snackbar = Snackbar
                .make(
                    findViewById(R.id.verificationRoot),
                    "اینترنت متصل نیست",
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction("بررسی مجدد ...") { view: View? -> finish();
                    overridePendingTransition(0,0)
                    startActivity(intent); }
            snackbar.show()
        }


        nextBtn.setOnClickListener {


            if (mobileNum != null && verificationCodeEt.text.toString().isNotEmpty()) {

                viewModel.verification(mobileNum, verificationCodeEt.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object :
                        NikoSingleObserver<Response<VerificationResponse>>(compositeDisposable) {
                        override fun onSuccess(t: Response<VerificationResponse>) {

                            val response = t.body()?.data

                            if (t.code()==200){

                                //for checking the driver position in registering

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
                                                    FillInfoActivity::class.java
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
                            }else{

                                runOnUiThread {
                                    kotlin.run {
                                        Toast.makeText(
                                            applicationContext,
                                            t.message(),
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

    override fun onBackPressed() {

    }
}