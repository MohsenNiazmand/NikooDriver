package com.example.nikodriver.feature.auth.upload_docs

import androidx.lifecycle.MutableLiveData
import com.example.nikodriver.common.NikoViewModel
import com.example.nikodriver.data.repositories.UploadDocsRepository
import com.example.nikodriver.data.submitDocsResponse.SubmitDocsResponse
import com.example.nikodriver.data.uploadDocResponse.UploadDocResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

class UploadDocsViewModel(val uploadDocsRepository: UploadDocsRepository) : NikoViewModel() {

    fun uploadDoc(title:String,doc: MultipartBody.Part): Single<Response<UploadDocResponse>>{
        progressBarLiveData.value = true
        return uploadDocsRepository.uploadDoc(title,doc).doFinally {
            progressBarLiveData.postValue(false)
        }
    }
    fun submitDocs(
                   carCardUrl:String,
                   certificateOfBadRecordUrl:String,
                   certificateUrl:String,
                   nationalCardUrl:String,
                   technicalDiagnosisUrl:String,
                   workBookUrl:String): Single<Response<SubmitDocsResponse>>{
        progressBarLiveData.value = true
        return uploadDocsRepository.submitDocs(carCardUrl,certificateOfBadRecordUrl,certificateUrl,nationalCardUrl,technicalDiagnosisUrl,workBookUrl).doFinally {
            progressBarLiveData.postValue(false)

        }

    }

}