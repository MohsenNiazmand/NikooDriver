package com.akaf.nikoodriver.data.repositories

import com.akaf.nikoodriver.data.repositories.sources.uploadDocs.UploadDocsDataSource
import com.akaf.nikoodriver.data.responses.submitDocsResponse.SubmitDocsResponse
import com.akaf.nikoodriver.data.responses.uploadDocResponse.UploadDocResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response


class UploadDocsRepositoryImpl(
    val uploadDocsLocalDataSource: UploadDocsDataSource,
    val uploadDocsRemoteDataSource: UploadDocsDataSource
) :UploadDocsRepository {
    override fun uploadDoc(

        title: String,
        doc: MultipartBody.Part
    ): Single<Response<UploadDocResponse>> {
        return uploadDocsRemoteDataSource.uploadDoc(title,doc)
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
        return uploadDocsRemoteDataSource.submitDocs(token,carCardUrl,certificateOfBadRecordUrl,certificateUrl,nationalCardUrl,technicalDiagnosisUrl,workBookUrl)
    }
}