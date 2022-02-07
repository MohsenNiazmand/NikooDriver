package com.akaf.nikoodriver.feature.main.transactions


import androidx.lifecycle.MutableLiveData
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.common.NikoViewModel
import com.akaf.nikoodriver.data.repositories.TransactionsRepository
import com.akaf.nikoodriver.data.responses.transactionsResponse.TransactionsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.KoinApiExtension
import retrofit2.Response
import timber.log.Timber

@KoinApiExtension
class TransactionsViewModel(val transactionsRepository: TransactionsRepository) : NikoViewModel() {

    val transactionsLiveData= MutableLiveData<Response<TransactionsResponse>>()


    init {
        getTransactions()
    }

    @KoinApiExtension
    fun getTransactions(){
        progressBarLiveData.value=true
        transactionsRepository.transactions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :NikoSingleObserver<Response<TransactionsResponse>>(compositeDisposable){
                override fun onSuccess(t: Response<TransactionsResponse>) {

                    if (t.isSuccessful){
                        progressBarLiveData.postValue(false)
                        transactionsLiveData.value=t
                    }else
                        progressBarLiveData.postValue(false)




                }
                override fun onError(e: Throwable) {
                    Timber.e(e)
                    progressBarLiveData.postValue(false)
                }

            })
    }
}