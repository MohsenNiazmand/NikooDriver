package com.example.nikodriver.data.repositories.sources.uploadDocs

import com.example.nikodriver.data.submitDocsResponse.SubmitDocsResponse
import com.example.nikodriver.data.uploadDocResponse.UploadDocResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

class UploadDocsLocalDataSource : UploadDocsDataSource {
    override fun uploadDoc(
        title: String,
        doc: MultipartBody.Part
    ): Single<Response<UploadDocResponse>> {
        TODO("Not yet implemented")
    }

    override fun submitDocs(
        carCardUrl: String,
        certificateOfBadRecordUrl: String,
        certificateUrl: String,
        nationalCardUrl: String,
        technicalDiagnosisUrl: String,
        workBookUrl: String
    ): Single<Response<SubmitDocsResponse>> {
        TODO("Not yet implemented")
    }
}