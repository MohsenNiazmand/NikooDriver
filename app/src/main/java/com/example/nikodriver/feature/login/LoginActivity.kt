package com.example.nikodriver.feature.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nikodriver.R
import com.example.nikodriver.common.NikoActivity
import com.example.nikodriver.common.NikoCompletableObserver
import com.example.nikodriver.feature.register.RegisterActivity
import com.example.nikodriver.feature.verification.VerificationActivity
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

            startActivity(Intent(this@LoginActivity, VerificationActivity::class.java))


//            viewModel.login(mobileEt.text.toString())
//                .subscribeOn(Schedulers.io())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : NikoCompletableObserver(compositeDisposable) {
//                    override fun onComplete() {
//                        startActivity(Intent(this@LoginActivity, VerificationActivity::class.java))
//                    }
//                })
        }
//
//        viewModel.progressBarLiveData.observe(this) {
//            setProgressIndicator(it)
//        }


    }

//    override fun onDestroy() {
//        super.onDestroy()
//        compositeDisposable.clear()
//
//    }

}