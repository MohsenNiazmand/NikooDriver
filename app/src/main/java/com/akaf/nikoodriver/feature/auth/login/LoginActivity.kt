package com.akaf.nikoodriver.feature.auth.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseActivity
import com.akaf.nikoodriver.common.NikoCompletableObserver
import com.akaf.nikoodriver.feature.auth.verification.VerificationActivity
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity() {
    val viewModel: LoginViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    lateinit var dexter: Dexter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        catchingPermissions()

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
            when {
                it-> loginBtn.visibility=View.INVISIBLE
                else-> loginBtn.visibility=View.VISIBLE
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()

    }


    private fun catchingPermissions() {
       val dexter = Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.VIBRATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.INTERNET
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        loginBtn.isClickable = true
                    } else {
                        loginBtn.isClickable = false
                        val snackbar = Snackbar
                            .make(
                                findViewById(R.id.loginRoot),
                                "برای استفاده از نرم افزار نیاز است اجازه دسترسی به امکانات مورد نظر داده شود",
                                Snackbar.LENGTH_INDEFINITE
                            )
                            .setAction("بررسی مجدد") { view: View? -> dexter.check() }
                        snackbar.show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }) as Dexter
        dexter.check()
    }


    override fun onBackPressed() {
    }

}