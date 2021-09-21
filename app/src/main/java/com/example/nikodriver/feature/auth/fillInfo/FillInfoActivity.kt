package com.example.nikodriver.feature.auth.fillInfo

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.nikodriver.R
import com.example.nikodriver.common.BaseActivity
import com.example.nikodriver.common.NikoSingleObserver
import com.example.nikodriver.data.TokenContainer
import com.example.nikodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.example.nikodriver.data.fillInfoResponse.FillInfoResponse
import com.example.nikodriver.data.verificationResponse.VerificationResponse
import com.example.nikodriver.feature.auth.chooseDialog.ChoosePictureDialog
import com.example.nikodriver.feature.auth.upload_docs.UploadDocsActivity
import com.example.nikodriver.services.createApiServiceInstance
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlinx.android.synthetic.main.activity_fill_info.*
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.net.URLEncoder


class FillInfoActivity: BaseActivity(),ChoosePictureDialog.ChooseOpinionsCallback {




    val viewModel: FillInfoViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    lateinit var vehicleType:String
    val token="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfdHlwZSI6ImRyaXZlciIsImlhdCI6MTYzMjExNTA1MywiZXhwIjoxNjMyMjg3ODUzLCJzdWIiOiIzOCJ9.vewgsL1PMVdEOREJq0w2rVrtHGBRZlax7MAnA3bjdtU"
    val driverProfileUrl = MutableLiveData<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_info)



        chooseDriverPicBtn.setOnClickListener {

            val chooseDialog = ChoosePictureDialog()
            chooseDialog.chooseOpinionsCallback=this
            chooseDialog.show(supportFragmentManager, null)

        }


        toggleBtnVehicleType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (carTypeBtn.isChecked){
                vehicleType="car"
            }else{
                vehicleType="bus"
            }
        }



        //Persian date picker
        insuranceExpireEt.setOnFocusChangeListener { view, b ->
            runOnUiThread {
                kotlin.run {
                    val picker = PersianDatePickerDialog(this)
                        .setPositiveButtonString("باشه")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("امروز")
                        .setTodayButtonVisible(true)
                        .setMinYear(1300)
                        .setAllButtonsTextSize(16)
                        .setPickerBackgroundColor(Color.WHITE)
                        .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                        .setInitDate(1374, 2, 1)
                        .setActionTextColor(Color.BLACK)
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(object : PersianPickerListener {
                            override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                                val issueDateTxt =
                                    persianPickerDate.persianYear.toString() + "/" + persianPickerDate.persianMonth + "/" + persianPickerDate.persianDay
                                insuranceExpireEt.setText(issueDateTxt)
                            }

                            override fun onDismissed() {
                                Toast.makeText(this@FillInfoActivity, "Dismissed", Toast.LENGTH_SHORT).show()
                            }
                        })

                    picker.show()


                }

            }

        }
        //for auto focusing next edittext
        firstPlaqueEtReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (firstPlaqueEtReg.getText().hashCode() == editable.hashCode()) {
                    if (firstPlaqueEtReg.length() == 2) {
                        thirdPlaqueNumEtReg.requestFocus()
                    }
                }
            }
        })

        thirdPlaqueNumEtReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (thirdPlaqueNumEtReg.getText().hashCode() == editable.hashCode()) {
                    if (thirdPlaqueNumEtReg.length() == 3) {
                        irPlaqueEtReg.requestFocus()
                    }
                }
            }
        })



        registerBtn.setOnClickListener {

            val plaque =
                "ایران" + " " + irPlaqueEtReg.text.toString() + " " + thirdPlaqueNumEtReg.text.toString() + " " + plaqueSpinner.selectedItem.toString() + " " + firstPlaqueEtReg.text.toString()


            //validation of fields
            if (firstNameEtReg.text.isNotEmpty() &&
                lastNameEtReg.text.isNotEmpty() &&
                nationalCodeEtReg.text.isNotEmpty() && nationalCodeEtReg.text.length == 10 &&
                certificateCodeEtReg.text.isNotEmpty() && certificateCodeEtReg.text.length == 10 &&
                firstPlaqueEtReg.text.isNotEmpty() && thirdPlaqueNumEtReg.text.isNotEmpty() && irPlaqueEtReg.text.isNotEmpty() &&
                vehicleType.isNotEmpty() &&
                vehicleColorEtReg.text.isNotEmpty() &&
                insuranceExpireEt.text.isNotEmpty() &&
                        driverProfileUrl.value!=null) {

                viewModel.register(firstNameEtReg.text.toString(),lastNameEtReg.text.toString(),nationalCodeEtReg.text.toString(),certificateCodeEtReg.text.toString(),driverProfileUrl.value.toString(),plaque,vehicleType,vehicleColorEtReg.text.toString(),insuranceExpireEt.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : NikoSingleObserver<Response<FillInfoResponse>>(compositeDisposable){
                        override fun onSuccess(t: Response<FillInfoResponse>) {

                            if (t.code()==200){
                                startActivity(Intent(this@FillInfoActivity, UploadDocsActivity::class.java))

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

                    } )

            }else{

                runOnUiThread {
                    kotlin.run {
                        Toast.makeText(
                            applicationContext,
                            "لطفا مشخصات را به درستی وارد نمایید",
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

    //it calls after image cropping for get back to this activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            openCropActivity(photoURI)
        }
        if (requestCode == REQ_CODE_CHOOSE_IMAGE_FROM_GALLERY && data != null) {
            val galleryPicUri = Uri.fromFile(data.data?.let { uriToFile(it) })
            openCropActivity(galleryPicUri)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            val result = CropImage.getActivityResult(data)
            val uri = result.uri
            if (uri != null) {
                val path = uri.path
                if ( path != null) {
                    val finalFileImage = File(path)
                    //upload



                    val body = finalFileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val formDataFile = MultipartBody.Part.createFormData("photo", URLEncoder.encode(finalFileImage.name, "utf-8"), body)

                    //uploading driver photo
                    viewModel.uploadDriverPhoto("driverPhoto",formDataFile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : NikoSingleObserver<Response<UploadPhotoDriverResponse>>(compositeDisposable){
                            override fun onSuccess(t: Response<UploadPhotoDriverResponse>) {

                                if (t.code()==200){
                                    driverImg.setImageURI(t.body()?.data?.url)
                                    driverProfileUrl.value=t.body()?.data?.url
                                    checkedProfile.visibility=View.VISIBLE

                                }



                            }

                        })


                }
            }

            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Timber.e(error)
            }
        }

    }

    override fun onCameraClicked() {
        CaptureImageFromCamera()
    }

    override fun onGalleryClick() {
        ChoosePictureFromGallery()
    }

    override fun onBackPressed() {
    }
}
