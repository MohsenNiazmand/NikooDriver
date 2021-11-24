package com.akaf.nikoodriver.feature.auth.registering

import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    var token:String=""
    var firstName:String=""
    var lastName:String=""
    var nationalCode:String=""
    var certificateCode:String=""
    var vehicleColor:String=""
    var insuranceExpire:String=""
    var serviceType:String=""
}