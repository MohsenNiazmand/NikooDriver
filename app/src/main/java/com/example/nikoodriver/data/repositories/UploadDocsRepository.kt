package com.example.nikoodriver.data.repositories

import com.example.nikoodriver.data.submitDocsResponse.SubmitDocsResponse
import com.example.nikoodriver.data.uploadDocResponse.UploadDocResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

interface UploadDocsRepository {
    fun uploadDoc(title:String,doc:MultipartBody.Part):Single<Response<UploadDocResponse>>
    fun submitDocs(carCardUrl:String,
                   certificateOfBadRecordUrl:String,
                   certificateUrl:String,
                   nationalCardUrl:String,
                   technicalDiagnosisUrl:String,
                   workBookUrl:String):Single<Response<SubmitDocsResponse>>

}