package com.febrian.covidapp.global

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import com.febrian.covidapp.global.DateUtil.getDate
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getDate(minus: Int): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("M/dd/yy")
        calendar.add(Calendar.DATE, minus)

        return dateFormat.format(calendar.time)
    }

    fun getDateStatistic(minus: Int): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd\nMMM")
        calendar.add(Calendar.DATE, minus)

        return dateFormat.format(calendar.time)
    }
}