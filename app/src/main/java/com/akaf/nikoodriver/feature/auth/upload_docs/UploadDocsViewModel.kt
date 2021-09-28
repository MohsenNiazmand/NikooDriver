package com.akaf.nikoodriver.feature.auth.upload_docs

import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.repositories.UploadDocsRepository
import com.akaf.nikoodriver.data.submitDocsResponse.SubmitDocsResponse
import com.akaf.nikoodriver.data.uploadDocResponse.UploadDocResponse
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
    fun submitDocs(token:String,
                   carCardUrl:String,
                   certificateOfBadRecordUrl:String,
                   certificateUrl:String,
                   nationalCardUrl:String,
                   technicalDiagnosisUrl:String,
                   workBookUrl:String): Single<Response<SubmitDocsResponse>>{
        progressBarLiveData.value = true
        return uploadDocsRepository.submitDocs(token,carCardUrl,certificateOfBadRecordUrl,certificateUrl,nationalCardUrl,technicalDiagnosisUrl,workBookUrl).doFinally {
            progressBarLiveData.postValue(false)

        }

    }

}