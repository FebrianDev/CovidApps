package com.febrian.covidapp.global.response

data class Response(
    val lastUpdate : Long?,
    val lat : Double?,
    val long : Double?,
    var confirmed : Int?,
    var recovered : Int?,
    var deaths : Int?,
    var active : Int?,
    val combinedKey : String?
)
