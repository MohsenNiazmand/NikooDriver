package com.example.nikoodriver.feature.auth.upload_docs

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.nikoodriver.R
import com.example.nikoodriver.common.BaseActivity
import com.example.nikoodriver.common.NikoSingleObserver
import com.example.nikoodriver.data.submitDocsResponse.SubmitDocsResponse
import com.example.nikoodriver.data.uploadDocResponse.UploadDocResponse
import com.example.nikoodriver.feature.auth.chooseDialog.ChoosePictureDialog
import com.example.nikoodriver.feature.auth.finishReg.FinishRegisterActivity
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_upload_docs.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.net.URLEncoder

class UploadDocsActivity() : BaseActivity(),ChoosePictureDialog.ChooseOpinionsCallback  {

    val picNum = MutableLiveData<Int>()
    val nationalCardId = MutableLiveData<String>()
    val carCardId = MutableLiveData<String>()
    val certificateCardId = MutableLiveData<String>()
    val badRecordsId = MutableLiveData<String>()
    val technicalDiagnosisId = MutableLiveData<String>()
    val workBookId = MutableLiveData<String>()
    val compositeDisposable= CompositeDisposable()
    val viewModel:UploadDocsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_docs)
        val token=intent!!.getStringExtra("token")




        uploadNationalCardGalleryBtn.setOnClickListener {
            ChoosePictureFromGallery()
            picNum.value=1
        }

        uploadNationalCardCameraBtn.setOnClickListener {
            CaptureImageFromCamera()
            picNum.value=1
        }

        uploadCarCardGalleryBtn.setOnClickListener {
            ChoosePictureFromGallery()
            picNum.value=2
        }

        uploadCarCardCameraBtn.setOnClickListener {
            CaptureImageFromCamera()
            picNum.value=2
        }


        uploadCertificateGalleryBtn.setOnClickListener {
            ChoosePictureFromGallery()
            picNum.value=3
        }

        uploadCertificateCameraBtn.setOnClickListener {
            CaptureImageFromCamera()
            picNum.value=3
        }

        uploadTechnicalDiagnosisGalleryBtn.setOnClickListener {
            ChoosePictureFromGallery()
            picNum.value=4
        }

        uploadTechnicalDiagnosisCameraBtn.setOnClickListener {
            CaptureImageFromCamera()
            picNum.value=4
        }

        uploadBadRecordsGalleryBtn.setOnClickListener {
            ChoosePictureFromGallery()
            picNum.value=5
        }

        uploadBadRecordsCameraBtn.setOnClickListener {
            CaptureImageFromCamera()
            picNum.value=5
        }


        uploadWorkbookGalleryBtn.setOnClickListener {
            ChoosePictureFromGallery()
            picNum.value=6
        }

        uploadWorkbookCameraBtn.setOnClickListener {
            CaptureImageFromCamera()
            picNum.value=6
        }

        proceedDocsBtn.setOnClickListener {


            if (token != null && carCardId.value!=null && badRecordsId.value!=null&&certificateCardId.value!=null&&nationalCardId.value!=null&&technicalDiagnosisId.value!=null&&workBookId.value!=null){

                    viewModel.submitDocs(token,carCardId.value.toString(),badRecordsId.value.toString(),certificateCardId.value.toString(),nationalCardId.value.toString(),technicalDiagnosisId.value.toString(),workBookId.value.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : NikoSingleObserver<Response<SubmitDocsResponse>>(compositeDisposable){
                            override fun onSuccess(t: Response<SubmitDocsResponse>) {
                                val responseCode=t.code()
                                if (responseCode==200){
                                    startActivity(Intent(this@UploadDocsActivity, FinishRegisterActivity::class.java))


                                }else{
                                    runOnUiThread {
                                        kotlin.run {
                                            Toast.makeText(
                                                applicationContext,
                                                t.message().toString(),
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }

                                    }
                                }

                            }

                        })


            }else {


                runOnUiThread {
                    kotlin.run {
                        Toast.makeText(
                            applicationContext,
                            "لطفا تمامی مدارک را بارگزاری کنید",
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
                    val  finalFileImage = File(path)
                    //upload
                    val body = finalFileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val formDataFile = MultipartBody.Part.createFormData("doc", URLEncoder.encode(finalFileImage.name, "utf-8"), body)
                    when(picNum.value){

                        1-> {
                            viewModel.uploadDoc("nationalCard",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if (t.code()==200){
                                            uploadedImageNationalCard.setImageURI(t.body()?.data?.url)
                                            nationalCardId.value=t.body()?.data?.id
                                            icCheckNationalCardUpload.visibility=View.VISIBLE
                                        }

                                    }


                                })
                        }

                        2-> {
                            viewModel.uploadDoc("carCard",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if (t.code()==200){
                                            uploadedImageCarCard.setImageURI(t.body()?.data?.url)
                                            carCardId.value=t.body()?.data?.id
                                            icCheckCarCardUpload.visibility=View.VISIBLE
                                        }

                                    }


                                })

                        }

                        3->{

                            viewModel.uploadDoc("certificate",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if (t.code()==200){
                                            uploadedImageCertificate.setImageURI(t.body()?.data?.url)
                                            certificateCardId.value=t.body()?.data?.id
                                            icCheckCertificateUpload.visibility=View.VISIBLE
                                        }

                                    }


                                })

                        }

                        4->{

                            viewModel.uploadDoc("technicalDiagnosis",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if(t.code()==200){
                                            uploadedImageTechnicalDiagnosis.setImageURI(t.body()?.data?.url)
                                            technicalDiagnosisId.value=t.body()?.data?.id
                                            icCheckTechnicalDiagnosisUpload.visibility=View.VISIBLE

                                        }

                                    }


                                })
                        }

                        5->{
                            viewModel.uploadDoc("certificateOfBadRecord",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if(t.code()==200){
                                            uploadedImageBadRecords.setImageURI(t.body()?.data?.url)
                                            badRecordsId.value=t.body()?.data?.id
                                            icCheckBadRecordsUpload.visibility=View.VISIBLE
                                        }

                                    }


                                })

                        }
                        6->{
                            viewModel.uploadDoc("workBook",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if (t.code()==200){
                                            uploadedImageWorkbook.setImageURI(t.body()?.data?.url)
                                            workBookId.value=t.body()?.data?.id
                                            icCheckWorkbookUpload.visibility=View.VISIBLE
                                        }

                                    }


                                })

                        }

                    }


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