package com.example.nikodriver.common

import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nikodriver.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

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


    val REQUEST_IMAGE_CAPTURE = 1
    val REQ_CODE_CHOOSE_IMAGE_FROM_GALLERY = 2
    val RESULT_OK = 10
    var currentPhotoPath: String? = null
    var image: File? = null
    var photoURI: Uri? = null
    open fun openCropActivity(uri: Uri?) {
        val cropper = CropImage.activity(uri)
//            .setAspectRatio(1, 1)
            .setOutputCompressQuality(70)
            .setFixAspectRatio(false)
            .setAllowRotation(true)
            .setAllowFlipping(true)
            .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
            .setMaxZoom(2)
            .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
            .start(this)
    }


    @SuppressLint("QueryPermissionsNeeded")
    open fun CaptureImageFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Toast.makeText(applicationContext, "error happend", Toast.LENGTH_SHORT).show()
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(
                    this,
                    "com.example.nikodriver.common.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }


    @Throws(IOException::class)
    open fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
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
            uri?.let { applicationContext.getContentResolver().query(it, items, null, null, null) }
        if (cursor != null) {
            cursor.moveToFirst()
            val picturePath = cursor.getString(cursor.getColumnIndexOrThrow(items[0]))
            cursor.close()
            return File(picturePath)
        }
        return null
    }



}


abstract class NikoFragment:Fragment(),NikoView{

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
}


abstract class NikoViewModel : ViewModel(){
    val compositeDisposable = CompositeDisposable()
    val progressBarLiveData = MutableLiveData<Boolean>()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}