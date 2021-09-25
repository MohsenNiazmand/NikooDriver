package com.example.nikoodriver.feature.auth.fillInfo

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.nikoodriver.R
import com.example.nikoodriver.common.BaseActivity
import com.example.nikoodriver.common.NikoSingleObserver
import com.example.nikoodriver.data.fillInfoResponse.DriverUploadPhotoResponse.UploadPhotoDriverResponse
import com.example.nikoodriver.data.fillInfoResponse.FillInfoResponse
import com.example.nikoodriver.feature.auth.chooseDialog.ChoosePictureDialog
import com.example.nikoodriver.feature.auth.upload_docs.UploadDocsActivity
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
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.net.URLEncoder


class FillInfoActivity: BaseActivity(),ChoosePictureDialog.ChooseOpinionsCallback {




    val viewModel: FillInfoViewModel by viewModel()
    val sharedPreferences: SharedPreferences by inject()
    val compositeDisposable = CompositeDisposable()
    lateinit var vehicleType:String
    val driverProfileUrl = MutableLiveData<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_info)
        val token=intent!!.getStringExtra("token")




        chooseDriverPicPart.setOnClickListener {

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
//        insuranceExpireEt.setOnFocusChangeListener { view, b ->
//            runOnUiThread {
//                kotlin.run {
//
//
//
//                }
//
//            }
//
//        }
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

                if (token != null) {
                    viewModel.register(token,firstNameEtReg.text.toString(),lastNameEtReg.text.toString(),nationalCodeEtReg.text.toString(),certificateCodeEtReg.text.toString(),driverProfileUrl.value.toString(),plaque,vehicleType,vehicleColorEtReg.text.toString(),insuranceExpireEt.text.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : NikoSingleObserver<Response<FillInfoResponse>>(compositeDisposable){
                            override fun onSuccess(t: Response<FillInfoResponse>) {

                                if (t.code()==200){
                                    startActivity(Intent(this@FillInfoActivity, UploadDocsActivity::class.java).apply {
                                        putExtra("token",token)
                                    })

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
                }

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


    //it calls after image cropping for get back to this activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            openCropActivity(photoURI)
        }
        if (requestCode == REQ_CODE_CHOOSE_IMAGE_FROM_GALLERY ) {

            val galleryPicUri = Uri.fromFile(uriToFile(data!!.data))
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
                    chooseDriverPicPart.visibility=View.INVISIBLE
                    pbProfile.visibility=View.VISIBLE
                    viewModel.uploadDriverPhoto("driverPhoto",formDataFile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : NikoSingleObserver<Response<UploadPhotoDriverResponse>>(compositeDisposable){
                            override fun onSuccess(t: Response<UploadPhotoDriverResponse>) {

                                if (t.code()==200){
                                    driverImg.setImageURI(uri)
                                    driverProfileUrl.value=t.body()?.data?.url
                                    if (t.body()?.data?.url!=null){
                                        pbProfile.visibility=View.GONE
                                        checkedProfile.visibility=View.VISIBLE
                                    }

                                }else{

                                    runOnUiThread {
                                        kotlin.run {
                                            Toast.makeText(
                                                applicationContext,
                                                "بارگزاری عکس ناموفق بود ، دوباره تلاش کنید",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }

                                    }
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

    fun datePicker(v: View){
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
