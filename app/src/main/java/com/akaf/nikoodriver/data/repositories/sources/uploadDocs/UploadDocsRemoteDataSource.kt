package com.akaf.nikoodriver.data.repositories.sources.uploadDocs

import com.akaf.nikoodriver.data.responses.submitDocsResponse.SubmitDocsResponse
import com.akaf.nikoodriver.data.responses.uploadDocResponse.UploadDocResponse
import com.akaf.nikoodriver.services.http.ApiService
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
        token:String,
        carCardUrl: String,
        certificateOfBadRecordUrl: String,
        certificateUrl: String,
        nationalCardUrl: String,
        technicalDiagnosisUrl: String,
        workBookUrl: String
    ): Single<Response<SubmitDocsResponse>> {
        return apiService.submitDocs(token,JsonObject().apply {
            addProperty("carCard",carCardUrl)
            addProperty("certificateOfBadRecord",certificateOfBadRecordUrl)
            addProperty("certificate",certificateUrl)
            addProperty("nationalCard",nationalCardUrl)
            addProperty("technicalDiagnosis",technicalDiagnosisUrl)
            addProperty("workBook",workBookUrl)
        })
    }
}