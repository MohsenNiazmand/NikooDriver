package com.akaf.nikoodriver.data.repositories.sources.uploadDocs

import com.akaf.nikoodriver.data.responses.submitDocsResponse.SubmitDocsResponse
import com.akaf.nikoodriver.data.responses.uploadDocResponse.UploadDocResponse
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
        token:String,
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