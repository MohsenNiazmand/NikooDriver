package com.example.nikodriver.feature.auth.upload_docs

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import com.example.nikodriver.R
import com.example.nikodriver.common.BaseActivity
import com.example.nikodriver.feature.auth.chooseDialog.ChoosePictureDialog
import com.example.nikodriver.feature.home.HomeActivity
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_upload_docs.*
import java.io.File

class UploadDocsActivity() : BaseActivity(),ChoosePictureDialog.ChooseOpinionsCallback  {

    val picNum = MutableLiveData<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_docs)


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
            startActivity(Intent(this@UploadDocsActivity, HomeActivity::class.java))

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
                    val  finalFileImageCarCard = File(path)
                    //upload
                    val finalPicture = BitmapFactory.decodeFile(finalFileImageCarCard.toString())

                    //set every picture on their related position
                    when (picNum.value){
                        1 -> uploadedImageNationalCard.setImageURI(uri)
                        2 -> uploadedImageCarCard.setImageURI(uri)
                        3 -> uploadedImageCertificate.setImageURI(uri)
                        4 -> uploadedImageCertificate.setImageURI(uri)
                        5 -> uploadedImageTechnicalDiagnosis.setImageURI(uri)
                        6 -> uploadedImageWorkbook.setImageURI(uri)
                    }


                }
            }

            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e("cropActivity", "onActivityResult: $error")
            }
        }

    }



    override fun onCameraClicked() {
        CaptureImageFromCamera()
    }

    override fun onGalleryClick() {
        ChoosePictureFromGallery()
    }
}