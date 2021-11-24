package com.akaf.nikoodriver.feature.auth.registering.upload_docs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.data.responses.submitDocsResponse.SubmitDocsResponse
import com.akaf.nikoodriver.data.responses.uploadDocResponse.UploadDocResponse
import com.akaf.nikoodriver.feature.auth.registering.AuthViewModel
import com.akaf.nikoodriver.feature.auth.registering.chooseDialog.ChoosePictureDialog
import com.akaf.nikoodriver.feature.auth.registering.fillInfo.FillInfoFragment
import com.akaf.nikoodriver.feature.auth.registering.finishReg.FinishRegisterFragment
import com.google.android.material.button.MaterialButton
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_verification.*
import kotlinx.android.synthetic.main.fragment_upload_docs.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.net.URLEncoder

class UploadDocsFragment() : BaseFragment(), ChoosePictureDialog.ChooseOpinionsCallback  {

    val picNum = MutableLiveData<Int>()
    val nationalCardId = MutableLiveData<String>()
    val carCardId = MutableLiveData<String>()
    val certificateCardId = MutableLiveData<String>()
    val badRecordsId = MutableLiveData<String>()
    val technicalDiagnosisId = MutableLiveData<String>()
    val workBookId = MutableLiveData<String>()
    val compositeDisposable= CompositeDisposable()
    val viewModel: UploadDocsViewModel by viewModel()
    var token:String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_upload_docs,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        token=authViewModel.token


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackDialog()
            }
        })

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

                viewModel.submitDocs(token!!,carCardId.value.toString(),badRecordsId.value.toString(),certificateCardId.value.toString(),nationalCardId.value.toString(),technicalDiagnosisId.value.toString(),workBookId.value.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : NikoSingleObserver<Response<SubmitDocsResponse>>(compositeDisposable){
                        override fun onSuccess(t: Response<SubmitDocsResponse>) {
                            val responseCode=t.code()
                            if (responseCode==200){
                                Navigation.findNavController(view)
                                    .navigate(R.id.action_uploadDocsFragment_to_finishRegisterFragment)


                            }else{
                                requireActivity().runOnUiThread {
                                    kotlin.run { Toast.makeText(requireContext(), t.message().toString(), Toast.LENGTH_SHORT).show()

                                    }

                                }
                            }

                        }

                    })


            }else {


               requireActivity().runOnUiThread {
                    kotlin.run { Toast.makeText(requireContext(), "لطفا تمامی مدارک را بارگزاری کنید", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
            when {
                it-> proceedDocsBtn.visibility=View.INVISIBLE
                else-> proceedDocsBtn.visibility=View.VISIBLE
            }
        }


    }



    private fun showBackDialog() {
        val backDialogView = layoutInflater.inflate(R.layout.dialog_back, null, false)
        val backDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        backDialog.setView(backDialogView)
        backDialog.setCancelable(false)
        backDialogView.findViewById<MaterialButton>(R.id.backBtnYes).setOnClickListener {
            backDialog.dismiss()
            view?.let { it1 -> Navigation.findNavController(it1).navigateUp() }


        }

        backDialogView.findViewById<MaterialButton>(R.id.backBtnNo).setOnClickListener {
            backDialog.dismiss()

        }
        backDialog.show()
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
//                    val body = finalFileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), finalFileImage)

                    val formDataFile = MultipartBody.Part.createFormData("doc", URLEncoder.encode(finalFileImage.name, "utf-8"), requestBody)
                    when(picNum.value){

                        1-> {
                            pbNationalCard.visibility=View.VISIBLE
                            uploadVectorNationalCard.visibility=View.GONE
                            viewModel.uploadDoc("nationalCard",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if (t.code()==200){
                                            uploadedImageNationalCard.setImageURI(uri)
                                            nationalCardId.value=t.body()?.data?.id
                                            pbNationalCard.visibility=View.GONE
                                            icCheckNationalCardUpload.visibility=View.VISIBLE
                                        }

                                    }


                                })
                        }

                        2-> {
                            uploadVectorCarCard.visibility=View.GONE
                            pbCarCard.visibility=View.VISIBLE
                            viewModel.uploadDoc("carCard",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if (t.code()==200){
                                            uploadedImageCarCard.setImageURI(uri)
                                            carCardId.value=t.body()?.data?.id
                                            pbCarCard.visibility=View.GONE
                                            icCheckCarCardUpload.visibility=View.VISIBLE
                                        }

                                    }


                                })

                        }

                        3->{
                            pbCertificate.visibility=View.VISIBLE
                            uploadVectorCertificate.visibility=View.GONE
                            viewModel.uploadDoc("certificate",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if (t.code()==200){
                                            uploadedImageCertificate.setImageURI(uri)
                                            certificateCardId.value=t.body()?.data?.id
                                            pbCertificate.visibility=View.GONE
                                            icCheckCertificateUpload.visibility=View.VISIBLE
                                        }

                                    }


                                })

                        }

                        4->{
                            pbTechnicalDiagnosis.visibility=View.VISIBLE
                            uploadVectorTechnicalDiagnosis.visibility=View.GONE
                            viewModel.uploadDoc("technicalDiagnosis",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if(t.code()==200){
                                            uploadedImageTechnicalDiagnosis.setImageURI(uri)
                                            technicalDiagnosisId.value=t.body()?.data?.id
                                            pbTechnicalDiagnosis.visibility=View.GONE
                                            icCheckTechnicalDiagnosisUpload.visibility=View.VISIBLE

                                        }

                                    }


                                })
                        }

                        5->{
                            pbBadRecords.visibility=View.VISIBLE
                            uploadVectorBadRecords.visibility=View.GONE
                            viewModel.uploadDoc("certificateOfBadRecord",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if(t.code()==200){
                                            uploadedImageBadRecords.setImageURI(uri)
                                            badRecordsId.value=t.body()?.data?.id
                                            pbBadRecords.visibility=View.GONE
                                            icCheckBadRecordsUpload.visibility=View.VISIBLE
                                        }

                                    }


                                })

                        }
                        6->{
                            pbWorkBook.visibility=View.VISIBLE
                            uploadVectorWorkbook.visibility=View.GONE
                            viewModel.uploadDoc("workBook",formDataFile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : NikoSingleObserver<Response<UploadDocResponse>>(compositeDisposable){
                                    override fun onSuccess(t: Response<UploadDocResponse>) {
                                        if (t.code()==200){
                                            uploadedImageWorkbook.setImageURI(uri)
                                            workBookId.value=t.body()?.data?.id
                                            pbWorkBook.visibility=View.GONE
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

}