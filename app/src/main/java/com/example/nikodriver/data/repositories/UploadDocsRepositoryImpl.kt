package com.example.nikodriver.data.repositories

import com.example.nikodriver.data.repositories.sources.uploadDocs.UploadDocsDataSource
import com.example.nikodriver.data.submitDocsResponse.SubmitDocsResponse
import com.example.nikodriver.data.uploadDocResponse.UploadDocResponse
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
        carCardUrl: String,
        certificateOfBadRecordUrl: String,
        certificateUrl: String,
        nationalCardUrl: String,
        technicalDiagnosisUrl: String,
        workBookUrl: String
    ): Single<Response<SubmitDocsResponse>> {
        return uploadDocsRemoteDataSource.submitDocs(carCardUrl,certificateOfBadRecordUrl,certificateUrl,nationalCardUrl,technicalDiagnosisUrl,workBookUrl)
    }
}