package com.akaf.nikoodriver.feature.main.offersHistory

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.akaf.nikoodriver.App
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.repositories.OffersHistoryRepository
import com.akaf.nikoodriver.data.responses.offersHistoryResponse.OffersHistoryResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class OffersHistoryViewModel(val offersHistoryRepository: OffersHistoryRepository) : NikoViewModel() {

    val offersHistoryLiveData=MutableLiveData<Response<OffersHistoryResponse>>()
    var page=1
    var offersHistoryResponse:OffersHistoryResponse?=null
    init {
        getOffersHistory()
    }

    fun getOffersHistory(){
        progressBarLiveData.value=true
        offersHistoryRepository.offersHistory(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<OffersHistoryResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<OffersHistoryResponse>) {
                    progressBarLiveData.postValue(false)
                    val response=t
                    offersHistoryLiveData.postValue(handleOffersHistoryResponse(response))
                }

                override fun onError(e: Throwable) {
                    progressBarLiveData.postValue(false)
                    Toast.makeText(App.context,"عملیات با خطا مواجه شد",Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun handleOffersHistoryResponse(response:Response<OffersHistoryResponse>) : Response<OffersHistoryResponse>? {
        if (response.isSuccessful){
            response.body()?.let {resultResponse ->
                page++
                if (offersHistoryResponse==null){
                    offersHistoryResponse=resultResponse
                }else{
                    val oldDocs=offersHistoryResponse?.data?.docs
                    val newDocs= resultResponse.data.docs
                    oldDocs?.addAll(newDocs)

                }
            return Response.success(offersHistoryResponse ?: resultResponse)
            }
        }
        return Response.error(response.code(),response.errorBody())
    }

}


sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}