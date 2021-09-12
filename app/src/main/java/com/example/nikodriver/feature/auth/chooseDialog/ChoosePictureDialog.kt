package com.example.nikodriver.feature.auth.chooseDialog

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.nikodriver.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_choose_picture.*
import java.io.File
import java.io.IOException


class ChoosePictureDialog : BottomSheetDialogFragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.bottom_sheet_choose_picture, container, false)

        cameraBtn.setOnClickListener {

        }

        return view
    }





}