package com.example.nikoodriver.feature.auth.chooseDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nikoodriver.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_choose_picture.view.*

//a test dialog for choosing picture
class ChoosePictureDialog() : BottomSheetDialogFragment() {

    var chooseOpinionsCallback :ChooseOpinionsCallback? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.bottom_sheet_choose_picture, container, false)

        view.cameraBtn.setOnClickListener {
            chooseOpinionsCallback?.onCameraClicked()
            dismiss()

        }

        view.galleryBtn.setOnClickListener {
            chooseOpinionsCallback?.onGalleryClick()
            dismiss()

        }

        return view
    }



    interface ChooseOpinionsCallback{
        fun onCameraClicked()
        fun onGalleryClick()

    }


}