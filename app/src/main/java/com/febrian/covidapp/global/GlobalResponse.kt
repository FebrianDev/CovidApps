package com.febrian.covidapp.global

import java.math.BigInteger

data class GlobalResponse(
    val cases: Int?,
    val recovered: Int?,
    val deaths: Int?,
    val updated : Long?
)