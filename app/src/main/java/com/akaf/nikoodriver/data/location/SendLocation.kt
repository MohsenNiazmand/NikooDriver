package com.akaf.nikoodriver.data.location

import com.google.gson.annotations.SerializedName

class SendLocation {
    @SerializedName("location")
    var location: ArrayList<Double> = ArrayList()
}