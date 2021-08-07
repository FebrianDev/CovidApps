package com.febrian.covidapp.api

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

    val globalDataCovid: ApiEndPoint = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_GLOBAL_DATA_COVID)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiEndPoint::class.java)

    val instance : ApiEndPoint by lazy {
        val service = Retrofit
            .Builder()
            .baseUrl("https://covid19.mathdro.id")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service.create(ApiEndPoint::class.java)
    }

    val newService : ApiEndPoint by lazy {
        val service = Retrofit
            .Builder()
            .baseUrl("https://disease.sh")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service.create(ApiEndPoint::class.java)
    }
}