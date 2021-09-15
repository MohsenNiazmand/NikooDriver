package com.example.nikodriver.feature.auth.upload_docs

import androidx.lifecycle.MutableLiveData
import com.example.nikodriver.common.NikoViewModel

class UploadDocsViewModel : NikoViewModel() {
    val picNum = MutableLiveData<Int>()

    private fun PicNum(){
        picNum.value=0
    }


}