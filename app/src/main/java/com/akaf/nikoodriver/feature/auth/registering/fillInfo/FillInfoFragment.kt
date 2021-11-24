package com.akaf.nikoodriver.feature.auth.registering.fillInfo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.common.BaseFragment
import com.akaf.nikoodriver.common.NikoSingleObserver
import com.akaf.nikoodriver.data.responses.fillInfoResponse.driverUploadPhotoResponse.UploadPhotoDriverResponse
import com.akaf.nikoodriver.data.responses.fillInfoResponse.FillInfoResponse
import com.akaf.nikoodriver.data.responses.serviceTypeResponse.Doc
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.feature.auth.registering.AuthViewModel
import com.akaf.nikoodriver.feature.auth.registering.chooseDialog.ChoosePictureDialog
import com.google.android.material.button.MaterialButton
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import kotlinx.android.synthetic.main.fragment_fill_info.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.net.URLEncoder


class FillInfoFragment: BaseFragment(), ChoosePictureDialog.ChooseOpinionsCallback,
    ServiceTypesDialog.PassData {




    val viewModel: FillInfoViewModel by viewModel()
    val sharedPreferences: SharedPreferences by inject()
    val compositeDisposable = CompositeDisposable()
    var vehicleType:String=""
    val driverProfileUrl = MutableLiveData<String>()
    var service_id:String=""
    var gregorian:String?=null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fill_info, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        val token=authViewModel.token
        firstNameEtReg.setText(authViewModel.firstName)
        lastNameEtReg.setText(authViewModel.lastName)
        nationalCodeEtReg.setText(authViewModel.nationalCode)
        certificateCodeEtReg.setText(authViewModel.certificateCode)
        vehicleColorEtReg.setText(authViewModel.vehicleColor)
        insuranceExpireEt.setText(authViewModel.insuranceExpire)
        serviceTypeTv.setText(authViewModel.serviceType)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackDialog()
            }
        })

        chooseDriverPicPart.setOnClickListener {
            val chooseDialog = ChoosePictureDialog()
            chooseDialog.chooseOpinionsCallback=this
            chooseDialog.show(childFragmentManager, null)

        }

        insuranceExpireEt.setOnClickListener { datePicker(view) }





        toggleBtnVehicleType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when{
                carTypeBtn.isChecked->vehicleType="car"
                busTypeBtn.isChecked->vehicleType="bus"
                vanTypeBtn.isChecked->vehicleType="van"
                miniBusTypeBtn.isChecked->vehicleType="minibus"
                otherTypeBtn.isChecked->vehicleType="all"
            }
        }

        //for auto focusing next edittext
        firstPlaqueEtReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (firstPlaqueEtReg.getText().hashCode() == editable.hashCode()) {
                    if (firstPlaqueEtReg.length() == 2) {
                        thirdPlaqueNumEtReg.requestFocus()
                    }
                }
            }
        })

        thirdPlaqueNumEtReg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (thirdPlaqueNumEtReg.getText().hashCode() == editable.hashCode()) {
                    if (thirdPlaqueNumEtReg.length() == 3) {
                        irPlaqueEtReg.requestFocus()
                    }
                }
            }
        })


        serviceTypeTv.setOnClickListener {
            val serviceTypesDialog= ServiceTypesDialog()
            serviceTypesDialog.show(childFragmentManager,null)
            serviceTypesDialog.passData=this

        }



        registerBtn.setOnClickListener {
            val plaque =
                "ایران" + " " + irPlaqueEtReg.text.toString() + " " + thirdPlaqueNumEtReg.text.toString() + " " + plaqueSpinner.selectedItem.toString() + " " + firstPlaqueEtReg.text.toString()


            //validation of fields
            if (firstNameEtReg.text.isNotEmpty() &&
                lastNameEtReg.text.isNotEmpty() &&
                nationalCodeEtReg.text.isNotEmpty() && nationalCodeEtReg.text.length == 10 &&
                certificateCodeEtReg.text.isNotEmpty() && certificateCodeEtReg.text.length == 10 &&
                firstPlaqueEtReg.text.isNotEmpty() && thirdPlaqueNumEtReg.text.isNotEmpty() && irPlaqueEtReg.text.isNotEmpty() &&
                vehicleType.isNotEmpty() &&
                vehicleColorEtReg.text.isNotEmpty() &&
                service_id.isNotEmpty()

            ) {
                authViewModel.firstName=firstNameEtReg.text.toString()
                authViewModel.lastName=lastNameEtReg.text.toString()
                authViewModel.nationalCode=nationalCodeEtReg.text.toString()
                authViewModel.certificateCode=certificateCodeEtReg.text.toString()
                authViewModel.vehicleColor=vehicleColorEtReg.text.toString()
                authViewModel.insuranceExpire=insuranceExpireEt.text.toString()
                authViewModel.serviceType=serviceTypeTv.text.toString()
                    viewModel.register(token,firstNameEtReg.text.toString(),lastNameEtReg.text.toString(),nationalCodeEtReg.text.toString(),certificateCodeEtReg.text.toString(),driverProfileUrl.value.toString(),plaque,vehicleType,vehicleColorEtReg.text.toString(),gregorian,
                        service_id.toInt())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : NikoSingleObserver<Response<FillInfoResponse>>(compositeDisposable){
                            override fun onSuccess(t: Response<FillInfoResponse>) {

                                if (t.code()==200){
                                    Navigation.findNavController(view)
                                        .navigate(R.id.action_fillInfoFragment_to_uploadDocsFragment)
                                }else{
                                    requireActivity().runOnUiThread { kotlin.run { Toast.makeText(requireContext(), t.message(), Toast.LENGTH_SHORT).show() } } }
                            }
                        } )
            }else{
                requireActivity().runOnUiThread { kotlin.run { Toast.makeText(requireContext(), "لطفا مشخصات را به درستی وارد نمایید", Toast.LENGTH_SHORT).show() } }
            }
        }

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
            when {
                it-> registerBtn.visibility=View.INVISIBLE
                else-> registerBtn.visibility=View.VISIBLE
            }
        }


    }


    
    //it calls after image cropping for get back to this activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            openCropActivity(photoURI)
        }
        if (requestCode == REQ_CODE_CHOOSE_IMAGE_FROM_GALLERY ) {
                if (uriToFile(data?.data)!=null){
                    val galleryPicUri = Uri.fromFile(uriToFile(data!!.data))
                    openCropActivity(galleryPicUri)
                }

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            val result = CropImage.getActivityResult(data)
            val uri = result.uri
            if (uri != null) {
                val path = uri.path
                if ( path != null) {
                    val finalFileImage = File(path)
                    //upload
//                    val body = finalFileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), finalFileImage)

                    val formDataFile = MultipartBody.Part.createFormData("photo", URLEncoder.encode(finalFileImage.name, "utf-8"), requestBody)

                    //uploading driver photo
                    chooseDriverPicPart.visibility=View.INVISIBLE
                    pbProfile.visibility=View.VISIBLE
                    viewModel.uploadDriverPhoto("driverPhoto",formDataFile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : NikoSingleObserver<Response<UploadPhotoDriverResponse>>(compositeDisposable){
                            override fun onSuccess(t: Response<UploadPhotoDriverResponse>) {

                                if (t.code()==200){
                                    driverImg.setImageURI(uri)
                                    driverProfileUrl.value=t.body()?.data?.url
                                    if (t.body()?.data?.url!=null){
                                        pbProfile.visibility=View.GONE
                                        checkedProfile.visibility=View.VISIBLE
                                    }

                                }else{
                                    requireActivity().runOnUiThread { kotlin.run { Toast.makeText(requireContext(), "بارگزاری عکس ناموفق بود ، دوباره تلاش کنید", Toast.LENGTH_SHORT).show() }
                                    }
                                }



                            }

                        })


                }
            }

            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Timber.e(error)
            }
        }

    }

    override fun onCameraClicked() {
        CaptureImageFromCamera()
    }

    override fun onGalleryClick() {
        ChoosePictureFromGallery()
    }


    private fun showBackDialog() {
        val backDialogView = layoutInflater.inflate(R.layout.dialog_back_fill_info, null, false)
        val backDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        backDialog.setView(backDialogView)
        backDialog.setCancelable(false)
        backDialogView.findViewById<MaterialButton>(R.id.backBtnYes).setOnClickListener {
            backDialog.dismiss()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))

        }

        backDialogView.findViewById<MaterialButton>(R.id.backBtnNo).setOnClickListener {
            backDialog.dismiss()

        }
        backDialog.show()
    }



    fun datePicker(v: View){
        val picker = PersianDatePickerDialog(requireContext())
            .setPositiveButtonString("باشه")
            .setNegativeButton("بیخیال")
            .setTodayButton("امروز")
            .setTodayButtonVisible(true)
            .setMinYear(1398)
            .setMaxYear(1450)
            .setAllButtonsTextSize(16)
            .setPickerBackgroundColor(Color.WHITE)
            .setInitDate(1400, 1, 1)
            .setActionTextColor(Color.BLACK)
            .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
            .setShowInBottomSheet(true)
            .setListener(object : PersianPickerListener {
                override fun onDateSelected(persianPickerDate: PersianPickerDate) {
                    val issueDateTxt =
                        persianPickerDate.persianYear.toString() + "/" + persianPickerDate.persianMonth + "/" + persianPickerDate.persianDay
                    gregorian=persianPickerDate.gregorianDate.toString()
                    insuranceExpireEt.setText(issueDateTxt)
                }

                override fun onDismissed() {
                    Toast.makeText(requireContext(), "Dismissed", Toast.LENGTH_SHORT).show()
                }
            })

        picker.show()
    }

    @SuppressLint("SetTextI18n")
    override fun selectedItem(doc: Doc) {
        serviceTypeTv.text=doc.name+" "+doc.description
        service_id=doc.id.toString()
    }


}
