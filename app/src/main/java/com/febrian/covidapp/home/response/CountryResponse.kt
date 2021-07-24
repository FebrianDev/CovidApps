package com.febrian.covidapp.home.response

import com.febrian.covidapp.global.DateUtil
import com.febrian.covidapp.global.DateUtil.*
import com.febrian.covidapp.global.DateUtils
import com.google.gson.annotations.SerializedName

data class CountryResponse(
    val confirmed : DetailCountryResponse?,
    val recovered : DetailCountryResponse?,
    val deaths : DetailCountryResponse?,
    val lastUpdate : String?
)