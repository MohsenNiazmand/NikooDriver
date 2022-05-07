package com.akaf.nikoodriver.feature.main.home

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.Navigation
import com.akaf.nikoodriver.App
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.data.responses.location.SendLocation
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.feature.main.home.credit.CreditDialog
import com.akaf.nikoodriver.services.DriverForegroundService
import com.akaf.nikoodriver.services.mqtt.HiveMqttManager
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import timber.log.Timber
import java.text.DecimalFormat

@KoinApiExtension
class HomeFragment : BaseFragment() {
    private val hiveMqttManager: HiveMqttManager by inject()
    val homeViewModel: HomeViewModel by viewModel()
    val sharedPreferences:SharedPreferences by inject()
    val compositeDisposable = CompositeDisposable()
    val handler = Handler()
    private val onlineStatus:Boolean
    get() = sharedPreferences.getBoolean("isOnline",false)




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onResume() {
        super.onResume()
        getProfile()

    }

    @KoinApiExtension
    @SuppressLint("SetTextI18n", "BinaryOperationInTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkMqtt()


        logoutBtn.setOnClickListener {
            showLogoutDialog()

        }

        activeBtn.setOnClickListener {
            if(CheckInternet() && CheckGps())
            showEmptySeatsDialog()
            else
                Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()

        }

        deActiveBtn.setOnClickListener {
            deActive()
            homeViewModel.setEmptySeats(0,false)
        }

        creditBtn.setOnClickListener {
            val creditDialog = CreditDialog()
           creditDialog.show(childFragmentManager, null)

//            val intent = Intent(context, HomeActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            }
//            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//
//            var builder = context?.let { it1 ->
//                NotificationCompat.Builder(it1, "NikooDriver")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("سرویس جدید")
//                    .setContentText("برای دریافت سرویس اینجا را لمس نمایید")
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setContentIntent(pendingIntent)
//                    .build()
//            }
//
//
//            val notificationManager =
//                context?.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
//            context?.let { it1 ->
//                NotificationManagerCompat.from(it1).apply {
//                    notificationManager.notify(100, builder)
//                }
//            }

        }


        offersHistoryBtn.setOnClickListener {
            if (CheckInternet()){
                Navigation.findNavController(view)
                    .navigate(R.id.action_homeFragment_to_offersHistoryFragment)
            } else
                Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()
        }


        transactionsBtn.setOnClickListener {
            if (CheckInternet()){
                Navigation.findNavController(view)
                    .navigate(R.id.action_homeFragment_to_transactionsFragment)
            } else
          Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()
        }

//        searchTravelBtn.setOnClickListener {
//            if(CheckInternet() && CheckGps()){
//                Navigation.findNavController(view)
//                    .navigate(R.id.action_homeFragment_to_travelRegistrationFragment)
//            }else
//                Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()
//
//
//        }

        unAcceptedPassengersBtn.setOnClickListener {
            if(CheckInternet() && CheckGps()){
                Navigation.findNavController(view)
                    .navigate(R.id.action_homeFragment_to_declinedPassengersFragment)
            }else
                Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()



        }

        currentTravelBtn.setOnClickListener {
            if(CheckInternet() && CheckGps()) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_homeFragment_to_currentTravelFragment)
            }else
                Toast.makeText(requireContext(),"لطفا مکان یاب و اینترنت خود را فعال کنید",Toast.LENGTH_SHORT).show()

        }

        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        homeViewModel.homeProgressBarLiveData.observe(viewLifecycleOwner){
            setProgressHome(it)
        }




    }


    @SuppressLint("SetTextI18n")
    @KoinApiExtension
    private fun getProfile(){

        when(onlineStatus){
            true->active()
            false->deActive()
        }




        userNameTv.text = homeViewModel.username
        if (homeViewModel.credit.isNotEmpty()){
            val credit =homeViewModel.credit.toDouble().toInt()/10
            creditTv.text=credit.toString()+" "+resources.getString(R.string.tooman)
        }
        if (homeViewModel.rate.isNotEmpty()){
            val rate=homeViewModel.rate.toDouble()
            val dec = DecimalFormat("#0.00")
            rateTv.text=dec.format(rate)
        }
        if (onlineStatus)
            emptySeatsTv.text=homeViewModel.emptySeats
        currentTripsCountTv.text=homeViewModel.currentTrips
        unAcceptedPassengersTv.text=homeViewModel.unAcceptedPassengers

        handler.postDelayed({
            homeViewModel.getProfile()
        },1500)


        if (getView()!=null)
            homeViewModel.profileLiveData.observe(viewLifecycleOwner){
                if (it.isSuccessful){

                    if (it?.body()?.data?.capacity==0)
                        deActive()

                    if (it!=null){

                        val fname=it.body()?.data?.fname
                        val lname=it.body()?.data?.lname
                        userNameTv.text= "$fname $lname"
                        val capa=it.body()?.data?.capacity
                        if (capa != null) {
                            if (capa >0){
                                if (onlineStatus){
                                    emptySeatsTv.visibility=View.VISIBLE
                                    emptySeatsTv.text=it.body()?.data?.capacity.toString()
                                }
                            }else if (capa == 0)
                                emptySeatsTv.visibility=View.GONE
                        }
                        val openTrips=it.body()?.data?.openTripsCount
                        if (openTrips != null) {
                            if (openTrips>0 ){
                                unAcceptedPassengersTv.visibility=View.VISIBLE
                                unAcceptedPassengersTv.text= openTrips.toString()
                            } else if (openTrips==0)
                                unAcceptedPassengersTv.visibility=View.GONE
                        }
                        val currents=it.body()?.data?.currentTripsCount
                        if (currents != null) {
                            if (currents>0){
                                currentTripsCountTv.visibility=View.VISIBLE
                                currentTripsCountTv.text=currents.toString()
                            }else if (currents==0){
                                currentTripsCountTv.visibility=View.GONE

                            }
                        }
                        if (it.body()?.data?.rate!=null){
                            val rate= it.body()?.data?.rate!!.toDouble()
                            val dec = DecimalFormat("#0.00")
                            rateTv.text=dec.format(rate)
                        }

                        if (it.body()?.data?.credit!=null){
                            val credit= it.body()?.data?.credit!!.toDouble().toInt()/10
                            creditTv.text=credit.toString()+" "+resources.getString(R.string.tooman)
                        }
                    }


                }else
                    Toast.makeText(activity?.applicationContext,"خطای ارتباط با سرور",Toast.LENGTH_SHORT).show()




            }



    }
    @KoinApiExtension
    private fun checkMqtt(){
        if (getView()!=null)
            homeViewModel.mqttState.observe(viewLifecycleOwner) {
                when(it){

                    true->{
                        checkPermStartLocationUpdate()
                        connectedSign.visibility=View.VISIBLE
                        disconnectSign.visibility=View.GONE
                    }

                    false->{
                        connectedSign.visibility=View.GONE
                        disconnectSign.visibility=View.VISIBLE

                    }

                }

            }
    }

    @KoinApiExtension
    private fun showLogoutDialog() {
        val logoutView = layoutInflater.inflate(R.layout.dialog_logout, null, false)
        val logoutDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        logoutDialog.setView(logoutView)
        logoutView.findViewById<MaterialButton>(R.id.logoutBtnYes).setOnClickListener {
            logoutDialog.dismiss()
            homeViewModel.clearSharedPreference()
            hiveMqttManager.disconnect()
            DriverForegroundService.stopService(requireContext())
            requireActivity().cacheDir.deleteRecursively()
            val intent=Intent(activity, LoginActivity::class.java).apply {
                action = "com.package.ACTION_LOGOUT"
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
        logoutView.findViewById<MaterialButton>(R.id.logoutBtnNo).setOnClickListener {
            logoutDialog.dismiss()
        }
        logoutDialog.show()
    }

    private fun active(){
        activationTv.visibility= View.GONE
        activeBtn.visibility= View.GONE
        deActiveBtn.visibility=View.VISIBLE
        emptySeatsTv.visibility=View.VISIBLE
        homeViewModel.setOnlineStatus(true)
        if (DriverForegroundService.instance==null)
            DriverForegroundService.startService(requireContext(),"Nikoo Driver")
    }

    private fun deActive(){
        activationTv.visibility= View.VISIBLE
        activeBtn.visibility= View.VISIBLE
        deActiveBtn.visibility=View.GONE
        emptySeatsTv.visibility=View.GONE
        homeViewModel.setOnlineStatus(false)
        stopLocationUpdates()
        if (DriverForegroundService.instance!=null)
            DriverForegroundService.stopService(requireContext())
    }


    @KoinApiExtension
    private fun showEmptySeatsDialog() {
        val emptySeatsView = layoutInflater.inflate(R.layout.dialog_empty_seats, null, false)
        val emptySeatsDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        emptySeatsDialog.setView(emptySeatsView)
        emptySeatsDialog.setCancelable(false)
        val emptySeatsEt= emptySeatsView.findViewById<EditText>(R.id.emptySeatsEt)
        emptySeatsEt.hint=resources.getString(R.string.maximumTropeCount)+" "+homeViewModel.maxCapacity
        emptySeatsView.findViewById<MaterialButton>(R.id.proceedEmptySeatsBtn).setOnClickListener {
            if (emptySeatsEt.text.isNotEmpty()&& emptySeatsEt.text.toString().toInt()<=homeViewModel.maxCapacity.toInt() &&emptySeatsEt.text.toString().toInt()!=0){
                homeViewModel.setEmptySeats(emptySeatsEt.text.toString().toInt(),true)
                emptySeatsTv.text=emptySeatsEt.text
                emptySeatsDialog.dismiss()
                handler.postDelayed({
                    homeViewModel.getProfile() },1500)
                active()
            }else
                Toast.makeText(activity,resources.getString(R.string.pleaseEnterTheRightCount),Toast.LENGTH_SHORT).show()
        }
        emptySeatsView.findViewById<MaterialButton>(R.id.cancelEmptySeatsBtn).setOnClickListener {
            emptySeatsDialog.dismiss()
        }
        emptySeatsDialog.show()
    }


    override var locationCallback = object : LocationCallback() {
        @SuppressLint("BinaryOperationInTimber")
        override fun onLocationResult(p0: LocationResult?) {
            if (p0 != null) {
                fusedLocation = p0.lastLocation
                val sendLocation = SendLocation()
                sendLocation.location.add(fusedLocation!!.latitude)
                sendLocation.location.add(fusedLocation!!.longitude)
                Timber.i("LOCATION DRIVER:"+" "+fusedLocation!!.latitude+" "+fusedLocation!!.longitude)
                homeViewModel.sendDriverLocation(fusedLocation!!)
                homeViewModel.sendDriverLocationToRest(sendLocation)
//                    checkFastLocUpdate(homeViewModel.currentTripLiveData.value)

            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
        }

    }

}