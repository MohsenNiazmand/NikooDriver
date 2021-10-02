

package com.akaf.nikoodriver.feature.auth.verification


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.data.verificationResponse.VerificationResponse
import com.akaf.nikoodriver.feature.auth.fillInfo.FillInfoActivity
import com.akaf.nikoodriver.feature.auth.upload_docs.UploadDocsActivity
import com.akaf.nikoodriver.feature.home.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_verification.*
import org.koin.android.viewmodel.ext.android.viewModel
import com.akaf.nikoodriver.common.BaseActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Response
import org.json.JSONObject
import java.lang.Exception


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
                            val token="Bearer "+t.body()?.data?.token
                            if (t.code()==200 && response != null){

                                //for checking the driver position in registering
                                    when (response.driver.status) {
                                        "active" ->{
                                            sendFireBaseToken()
                                            finishAffinity()
                                            startActivity(Intent(this@VerificationActivity, HomeActivity::class.java)) }
                                        "fillInfo" ->{ startActivity(Intent(this@VerificationActivity, FillInfoActivity::class.java).apply { putExtra("token",token) }) }
                                        "insufficient_docs" ->{ startActivity(Intent(this@VerificationActivity, UploadDocsActivity::class.java).apply { putExtra("token",token) }) }
                                        "blocked" ->
                                            runOnUiThread { kotlin.run { Toast.makeText(applicationContext, "اکانت شما مسدود شده است", Toast.LENGTH_SHORT).show() } }
                                        "company_deactivated" ->
                                            runOnUiThread { kotlin.run { Toast.makeText(applicationContext, "از سمت شرکت مسدود شدید", Toast.LENGTH_SHORT).show() } }
                                        else ->
                                            runOnUiThread { kotlin.run { Toast.makeText(applicationContext, "با پشتیبانی تماس بگیرید", Toast.LENGTH_SHORT).show() } }
                                    }

                            }else if (t.code()==403 && t.body()?.data==null){
                                try {
                                    val jObjError = JSONObject(t.errorBody()!!.string())
                                    runOnUiThread { kotlin.run {Toast.makeText(applicationContext, jObjError.getString("message"), Toast.LENGTH_LONG).show()}}
                                } catch (e: Exception) {
                                        runOnUiThread { kotlin.run {Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()}}
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


    private fun sendFireBaseToken() {
        FirebaseMessaging.getInstance().subscribeToTopic("Drivers")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener
            val token = task.result
            viewModel.sendFcmToken(token)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }



    override fun onBackPressed() {

    }
}