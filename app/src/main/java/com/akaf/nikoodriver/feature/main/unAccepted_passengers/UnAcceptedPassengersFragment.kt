package com.akaf.nikoodriver.feature.main.unAccepted_passengers

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.data.responses.unAcceptedPassengers.UnAcceptedPassengersData
import com.akaf.nikoodriver.feature.main.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_declined_passengers.*
import org.koin.android.ext.android.inject


class UnAcceptedPassengersFragment : BaseFragment(),UnAcceptedPassengersAdapter.UnAcceptedPassengersCallBacks {
    val unAcceptedPassengersViewModel:UnAcceptedPassengersViewModel by inject()
    val homeViewModel: HomeViewModel by inject()
    val sharedPreferences:SharedPreferences by inject()
    val unAcceptedPassengersAdapter=UnAcceptedPassengersAdapter()
    val handler = Handler()

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
            rvUnAcceptedPassengers.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            rvUnAcceptedPassengers.adapter=unAcceptedPassengersAdapter
                unAcceptedPassengersAdapter.unAcceptedPassengersCallBacks = this

                unAcceptedPassengersAdapter.passengers =
                    it.data as ArrayList<UnAcceptedPassengersData>



            when(it.data?.size){
                0->ivEmpty.visibility=View.VISIBLE
                else->ivEmpty.visibility=View.GONE

            }

        }



        unAcceptedPassengersViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

    }

    override fun onRejectBtnClicked(passenger: UnAcceptedPassengersData) {
        homeViewModel.rejectTrip(passenger.id)
        homeViewModel.rejectTripLiveData.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                handler.postDelayed({unAcceptedPassengersViewModel.unAcceptedPassengers()},1000)
            }
        }
    }

    override fun onAcceptBtnClicked(passenger: UnAcceptedPassengersData) {
        homeViewModel.acceptTrip(passenger.id,-1)
        homeViewModel.acceptTripLiveData.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                handler.postDelayed({unAcceptedPassengersViewModel.unAcceptedPassengers()},1000)
            }
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    override fun onNavigationClicked(passenger: UnAcceptedPassengersData) {
        val lat=passenger.Source.location.coordinates.get(0)
        val lng=passenger.Source.location.coordinates.get(1)
        val gmmIntentUri = Uri.parse("geo:${lat},${lng}?q=${lat},${lng}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)
    }

    override fun onCallClicked(passenger: UnAcceptedPassengersData) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${passenger.phoneNumber}")
        }
        if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)
        }
    }
}