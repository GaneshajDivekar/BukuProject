package com.example.demoproject.data.api.usermodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Ad {
    @SerializedName("company")
    @Expose
    var company: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("text")
    @Expose
    var text: String? = null


}