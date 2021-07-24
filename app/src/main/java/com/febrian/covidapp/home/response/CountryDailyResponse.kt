package com.febrian.covidapp.home.response

data class CountryDailyResponse(
    val countryRegion : String?,
    val confirmed : String?,
    val deaths : String?,
    val recovered : String?,
    val lastUpdate:String?
)
