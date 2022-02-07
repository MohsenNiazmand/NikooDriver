package com.akaf.nikoodriver.feature.main.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.data.responses.transactionsResponse.TransactionsData
import kotlinx.android.synthetic.main.fragment_transactions.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class TransactionsFragment : BaseFragment() {
    private val transactionsViewModel:TransactionsViewModel by viewModel()
    private val transactionsAdapter=TransactionsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTransactions.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rvTransactions.adapter=transactionsAdapter
        transactionsViewModel.transactionsLiveData.observe(viewLifecycleOwner){
            transactionsAdapter.transactions = it.body()?.data as ArrayList<TransactionsData>

            when(it.body()!!.data.size){
                0->ivEmptyT.visibility=View.VISIBLE
                else->ivEmptyT.visibility=View.GONE

            }

        }

        transactionsViewModel.progressBarLiveData.observe(viewLifecycleOwner){
            setProgressIndicator(it)
        }
    }

}