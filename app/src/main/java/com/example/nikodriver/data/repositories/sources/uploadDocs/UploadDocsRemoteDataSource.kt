package com.example.nikodriver.data.repositories.sources.uploadDocs

import com.example.nikodriver.data.submitDocsResponse.SubmitDocsResponse
import com.example.nikodriver.data.uploadDocResponse.UploadDocResponse
import com.example.nikodriver.services.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

class UploadDocsRemoteDataSource(val apiService: ApiService) : UploadDocsDataSource{
    override fun uploadDoc(
        title: String,
        doc: MultipartBody.Part
    ): Single<Response<UploadDocResponse>> {
        return apiService.uploadDoc(title,doc)
    }

    override fun submitDocs(
        carCardUrl: String,
        certificateOfBadRecordUrl: String,
        certificateUrl: String,
        nationalCardUrl: String,
        technicalDiagnosisUrl: String,
        workBookUrl: String
    ): Single<Response<SubmitDocsResponse>> {
        return apiService.submitDocs(JsonObject().apply {
            addProperty("carCard",carCardUrl)
            addProperty("certificateOfBadRecord",certificateOfBadRecordUrl)
            addProperty("certificate",certificateUrl)
            addProperty("nationalCard",nationalCardUrl)
            addProperty("technicalDiagnosis",technicalDiagnosisUrl)
            addProperty("workBook",workBookUrl)
        })
    }
}