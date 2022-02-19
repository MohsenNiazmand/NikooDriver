package com.akaf.nikoodriver.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akaf.nikoodriver.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.content.ClipData
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


//this class is the base of our project and other classes extend this

abstract class BaseActivity:AppCompatActivity(),NikoView{
        override val rootView: CoordinatorLayout?
        get() {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup !is CoordinatorLayout) {
                viewGroup.children.forEach {
                    if (it is CoordinatorLayout)
                        return it
                }
                throw IllegalStateException("RootView must be instance of CoordinatorLayout")
            } else
                return viewGroup
        }
    override val viewContext: Context?
        get() = this




    open fun CheckInternet(): Boolean {
        val wifiConnected: Boolean
        val mobileConnected: Boolean
        var returns = false
        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeInfo = connMgr.activeNetworkInfo
        if (activeInfo != null && activeInfo.isConnected) {
            wifiConnected = activeInfo.type == ConnectivityManager.TYPE_WIFI
            mobileConnected = activeInfo.type == ConnectivityManager.TYPE_MOBILE
            if (wifiConnected) {
                //Connected with Wifi
            } else if (mobileConnected) {
                //Connected with Mobile data
            }
            returns = true
        } else {

            returns = false
        }
        return returns
    }



    open fun CheckGps(): Boolean {

        var status: Boolean =true

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        }

        return status

    }



}


abstract class BaseFragment:Fragment(),NikoView{


    val REQUEST_IMAGE_CAPTURE = 1
    val REQ_CODE_CHOOSE_IMAGE_FROM_GALLERY = 2
    val RESULT_OK = 10
    var currentPhotoPath: String? = null
    var image: File? = null
    var photoURI: Uri? = null

    var fusedLocation: Location? = null
    var isFastLocation = false
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout
    override val viewContext: Context?
        get() = context



    open fun openCropActivity(uri: Uri?) {
        viewContext?.let {
            CropImage.activity(uri)
                .setOutputCompressQuality(70)
                .setFixAspectRatio(false)
                .setAllowRotation(true)
                .setAllowFlipping(true)
                .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                .setMaxZoom(2)
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .start(it,this)
        }
    }


    @SuppressLint("QueryPermissionsNeeded")
    open fun CaptureImageFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create the File where the photo should go
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            Toast.makeText(requireContext(), "error happend", Toast.LENGTH_SHORT).show()
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.akaf.nikoodriver.common.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                takePictureIntent.clipData = ClipData.newRawUri("", photoURI)
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    open fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath()
        return image
    }

    open fun ChoosePictureFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(
                galleryIntent,
                "انتخاب گالری"
            ), REQ_CODE_CHOOSE_IMAGE_FROM_GALLERY
        )
    }


    open fun uriToFile(uri: Uri?): File? {
        val items = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            uri?.let { requireContext().contentResolver.query(it, items, null, null, null) }
        if (cursor != null) {
            cursor.moveToFirst()
            val picturePath = cursor.getString(cursor.getColumnIndexOrThrow(items[0]))
            cursor.close()
            return File(picturePath)
        }
        return null
    }

    open fun CheckInternet(): Boolean {
        val wifiConnected: Boolean
        val mobileConnected: Boolean
        var returns = false
        val connMgr = requireContext().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeInfo = connMgr.activeNetworkInfo
        if (activeInfo != null && activeInfo.isConnected) {
            wifiConnected = activeInfo.type == ConnectivityManager.TYPE_WIFI
            mobileConnected = activeInfo.type == ConnectivityManager.TYPE_MOBILE
            if (wifiConnected) {
                //Connected with Wifi
            } else if (mobileConnected) {
                //Connected with Mobile data
            }
            returns = true
        } else {

            returns = false
        }
        return returns
    }



    open fun CheckGps(): Boolean {

        var status: Boolean =true

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){

            val locationManager = requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

            status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        }

        return status

    }


    fun checkPermStartLocationUpdate() {
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0?.areAllPermissionsGranted() == true) {
                        startLocationUpdates()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {

        if (!CheckGps()) {
            return
        }
        else if (CheckGps()){
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }

    var locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 60 * 30 * 1000
        smallestDisplacement = 100f
        this.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    open var locationCallback = object : LocationCallback() {
        @SuppressLint("BinaryOperationInTimber")
        override fun onLocationResult(p0: LocationResult?) {

        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
        }

    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }


}


interface NikoView{
    val rootView: CoordinatorLayout?
    val viewContext: Context?
    fun setProgressIndicator(mustShow: Boolean) {
        rootView?.let {
            viewContext?.let { context ->
                var loadingView = it.findViewById<View>(R.id.loadingView)
                if (loadingView == null && mustShow) {
                    loadingView =
                        LayoutInflater.from(context).inflate(R.layout.view_loading, it, false)
                    it.addView(loadingView)

                }

                loadingView?.visibility = if (mustShow) View.VISIBLE else View.GONE

            }

        }
    }
    fun setProgressHome(mustShow: Boolean) {
        rootView?.let {
            viewContext?.let { context ->
                var loadingView = it.findViewById<View>(R.id.homeLoading)
                if (loadingView == null && mustShow) {
                    loadingView =
                        LayoutInflater.from(context).inflate(R.layout.view_loading_home, it, false)
                    it.addView(loadingView)

                }

                loadingView?.visibility = if (mustShow) View.VISIBLE else View.GONE

            }

        }
    }
}


abstract class NikoViewModel : ViewModel(){
    val compositeDisposable = CompositeDisposable()
    val progressBarLiveData = MutableLiveData<Boolean>()
    val homeProgressBarLiveData = MutableLiveData<Boolean>()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}