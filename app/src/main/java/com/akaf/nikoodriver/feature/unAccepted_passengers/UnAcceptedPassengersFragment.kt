package com.akaf.nikoodriver.feature.unAccepted_passengers

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.data.responses.UnAcceptedPassengers.UnAcceptedPassengersData
import com.akaf.nikoodriver.feature.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.rvTrips
import kotlinx.android.synthetic.main.fragment_current_travel.*
import kotlinx.android.synthetic.main.fragment_declined_passengers.*
import org.koin.android.ext.android.inject
import timber.log.Timber
import kotlin.coroutines.coroutineContext


class UnAcceptedPassengersFragment : BaseFragment(),UnAcceptedPassengersAdapter.UnAcceptedPassengersCallBacks {
    val unAcceptedPassengersViewModel:UnAcceptedPassengersViewModel by inject()
    val homeViewModel:HomeViewModel by inject()
    val sharedPreferences:SharedPreferences by inject()
    val unAcceptedPassengersAdapter=UnAcceptedPassengersAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_declined_passengers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvUnAcceptedPassengers.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rvUnAcceptedPassengers.adapter=unAcceptedPassengersAdapter
        unAcceptedPassengersViewModel.unAcceptedPassengersResponse.observe(viewLifecycleOwner){
            if (it.data?.isEmpty() == true){
                ivEmpty.visibility=View.VISIBLE
            }else {
                ivEmpty.visibility=View.GONE
                unAcceptedPassengersAdapter.unAcceptedPassengersCallBacks = this
                Timber.i("unAcceptedPassengersResponse" + " " + it.toString())
                unAcceptedPassengersAdapter.passengers =
                    it.data as ArrayList<UnAcceptedPassengersData>
            }
        }

        unAcceptedPassengersViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

    }

    override fun onRejectBtnClicked(passenger: UnAcceptedPassengersData) {
        homeViewModel.rejectTrip(passenger.id)
    }

    override fun onAcceptBtnClicked(passenger: UnAcceptedPassengersData) {
        homeViewModel.acceptTrip(passenger.id,-1)
//        val count=sharedPreferences.getInt("seatsCount",0)
//        homeViewModel.decreaseSeatsCount(count)
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