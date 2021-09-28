package com.akaf.nikoodriver.data.repositories.sources.uploadDocs

import com.akaf.nikoodriver.data.submitDocsResponse.SubmitDocsResponse
import com.akaf.nikoodriver.data.uploadDocResponse.UploadDocResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

interface UploadDocsDataSource {
    fun uploadDoc(title:String,doc: MultipartBody.Part): Single<Response<UploadDocResponse>>
    fun submitDocs(token:String,
                   carCardUrl:String,
                   certificateOfBadRecordUrl:String,
                   certificateUrl:String,
                   nationalCardUrl:String,
                   technicalDiagnosisUrl:String,
                   workBookUrl:String):Single<Response<SubmitDocsResponse>>
}