package com.febrian.covidapp.home.response

data class CountryResponse(
    val confirmed : DetailCountryResponse?,
    val recovered : DetailCountryResponse?,
    val deaths : DetailCountryResponse?,
    val lastUpdate : String?
)
