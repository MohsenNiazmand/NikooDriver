package com.akaf.nikoodriver.feature.declined_passengers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import org.koin.android.ext.android.inject
import timber.log.Timber


class UnAcceptedPassengersFragment : BaseFragment() {
    val unAcceptedPassengersViewModel:UnAcceptedPassengersViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_declined_passengers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unAcceptedPassengersViewModel.unAcceptedPassengersResponse.observe(viewLifecycleOwner){
            if (it!=null)
            Timber.i("unAcceptedPassengersResponse"+" "+it.toString())
        }


    }
}