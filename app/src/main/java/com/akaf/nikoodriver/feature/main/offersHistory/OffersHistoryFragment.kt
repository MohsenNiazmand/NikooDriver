package com.akaf.nikoodriver.feature.main.offersHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_offers_history.*
import kotlinx.android.synthetic.main.fragment_transactions.*
import org.koin.android.ext.android.inject
import retrofit2.Response
import timber.log.Timber

class OffersHistoryFragment : BaseFragment() {
    val offersHistoryViewModel:OffersHistoryViewModel by inject()
    lateinit var offersHistoryAdapter:OffersHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_offers_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        offersHistoryViewModel.progressBarLiveData.observe(viewLifecycleOwner){
            setProgressIndicator(it)
        }

        offersHistoryViewModel.offersHistoryLiveData.observe(viewLifecycleOwner){
            showProgressBar()
            if (it.isSuccessful){
                hideProgressBar()
                it.body()?.data.let {newResponse ->
                    offersHistoryAdapter.differ.submitList(it.body()?.data?.docs?.toList())
                    val totalPages=
                        (it.body()?.data?.total?.div(10))?.plus(2) //query page size = 20
                    isLastPage=offersHistoryViewModel.page==totalPages
                }

            }else if(!it.isSuccessful){
                hideProgressBar()
                it.message().let { message->
                    Timber.e(message)
                }

            }

            when(it.body()!!.data.docs.size){
                0->ivEmptyH.visibility=View.VISIBLE
                else->ivEmptyH.visibility=View.GONE

            }
        }
    }

    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    val scrollListener = object :RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNotLastPage=!isLoading && !isLastPage
            val isAtLastItem=firstVisibleItemPosition+visibleItemCount >= totalItemCount
            val isNotAtBeginning=firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible=totalItemCount >= 10 //page size
            val shouldPaginate=isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                offersHistoryViewModel.getOffersHistory()
                isScrolling=false
            }

        }
    }

    private fun hideProgressBar(){
        progressBarHistory.visibility=View.GONE
        isLoading=false
    }

    private fun showProgressBar(){
        progressBarHistory.visibility=View.VISIBLE
        isLoading=true
    }


    fun setupRecyclerView(){
        offersHistoryAdapter=OffersHistoryAdapter()
        rvOffersHistory.apply {
            adapter=offersHistoryAdapter
            layoutManager=LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
            addOnScrollListener(this@OffersHistoryFragment.scrollListener)
        }
    }

}