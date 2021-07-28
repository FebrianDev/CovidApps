package com.febrian.covidapp.global.response

data class Response(
    val lastUpdate : Long?,
    val lat : Double?,
    val long : Double?,
    val confirmed : Int?,
    val recovered : Int?,
    val deaths : Int?,
    val active : Int?,
    val combinedKey : String?
)
