package com.febrian.covidapp.api

import com.febrian.covidapp.Response
import com.febrian.covidapp.news.data.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndPoint {

    @GET("/api/provinsi")
    fun getProvince() : Call<Response>

    @GET("/v2/top-headlines")
    fun getNews(
        @Query("q") q : String,
        @Query("country") country : String,
        @Query("category") category : String,
        @Query("apiKey") apiKey : String
    ) : Call<NewsResponse>
}