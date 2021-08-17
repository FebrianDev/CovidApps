package com.febrian.covidapp.global

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getDate(minus: Int): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("M/d/yy")
        calendar.add(Calendar.DATE, minus)

        return dateFormat.format(calendar.time)
    }

    fun getDateStatistic(minus: Int): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("d-M")
        calendar.add(Calendar.DATE, minus)

        return dateFormat.format(calendar.time)
    }
}