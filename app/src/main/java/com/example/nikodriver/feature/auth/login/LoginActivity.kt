package com.example.nikodriver.feature.auth.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.example.nikodriver.R
import com.example.nikodriver.common.BaseActivity
import com.example.nikodriver.common.NikoCompletableObserver
import com.example.nikodriver.feature.auth.verification.VerificationActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View
import com.example.nikodriver.feature.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject


class LoginActivity : BaseActivity() {
    val viewModel: LoginViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if (!CheckInternet()){

            val snackbar = Snackbar
                .make(
                    findViewById(R.id.loginRoot),
                    "اینترنت متصل نیست",
                    Snackbar.LENGTH_INDEFINITE
                )
                .setAction("بررسی مجدد ...") { view: View? -> finish();
                    startActivity(intent);
                }
            snackbar.show()
        }


        var pInfo: PackageInfo? = null
        try {
            pInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val version = pInfo!!.versionName
        versionTv.text = version




        loginBtn.setOnClickListener {

            val mobileNumber=mobileEt.text.toString()
            //this regex uses for iranian mobile number validation
            val regex=("(\\+98|0)?9\\d{9}").toRegex()
            val match=regex.matches(mobileNumber)
            if (match){
                viewModel.login(mobileNumber)
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : NikoCompletableObserver(compositeDisposable) {
                        override fun onComplete() {
                            val intent=Intent(this@LoginActivity, VerificationActivity::class.java).apply {
                                putExtra("MOBILE_NUM",mobileNumber)
                            }
                            startActivity(intent)
                        }
                    })
            }else{

                runOnUiThread {
                    kotlin.run {
                        Toast.makeText(
                            applicationContext,
                            "لطفا شماره موبایل را به درستی وارد کنید",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
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