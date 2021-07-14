package com.febrian.covidapp.api

import com.febrian.covidapp.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    val dataCovid: ApiEndPoint by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_DATA_COVID)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiEndPoint::class.java)
    }

    val newsCovid : ApiEndPoint by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_BERITA_COVID)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiEndPoint::class.java)
    }

    val globalDataCovid: ApiEndPoint by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_GLOBAL_DATA_COVID)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiEndPoint::class.java)
    }
}