package com.example.nikodriver.feature.auth.login

import android.content.Intent
import android.os.Bundle
import com.example.nikodriver.R
import com.example.nikodriver.common.NikoActivity
import com.example.nikodriver.common.NikoCompletableObserver
import com.example.nikodriver.feature.auth.verification.VerificationActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : NikoActivity() {
    val viewModel: LoginViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener {


            viewModel.login(mobileEt.text.toString())
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikoCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        val intent=Intent(this@LoginActivity, VerificationActivity::class.java).apply {
                            putExtra("MOBILE_NUM",mobileEt.text.toString())
                        }
                        startActivity(intent)
                    }
                })
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