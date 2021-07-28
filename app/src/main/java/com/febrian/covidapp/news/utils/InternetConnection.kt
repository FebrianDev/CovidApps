package com.febrian.covidapp.news.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object InternetConnection {
    fun isConnected(context: Context) : Boolean{
        val manager : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network : NetworkInfo? = manager.activeNetworkInfo

        return network!= null && network.isConnected
    }
}