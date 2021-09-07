package com.example.nikodriver.common

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class NikoActivity:AppCompatActivity(),NikoView{
    override fun setProgressIndicator(mustShow: Boolean) {
        TODO("Not yet implemented")
    }
}


abstract class NikoFragment:Fragment(),NikoView{
    override fun setProgressIndicator(mustShow: Boolean) {
        TODO("Not yet implemented")
    }
}


interface NikoView{
    fun setProgressIndicator(mustShow:Boolean)

}


abstract class NikoViewModel