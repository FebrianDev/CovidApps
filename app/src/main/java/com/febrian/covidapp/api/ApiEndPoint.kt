package com.febrian.covidapp.api

import com.febrian.covidapp.global.GlobalResponse
import com.febrian.covidapp.home.Response
import com.febrian.covidapp.home.response.CountryDailyResponse
import com.febrian.covidapp.home.response.CountryNameResponse
import com.febrian.covidapp.home.response.CountryResponse
import com.febrian.covidapp.home.response.ListCountryDailyResponse
import com.febrian.covidapp.news.data.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.febrian.covidapp.global.response.Response as ProvinceResponse
import retrofit2.Response as ResponseRetrofit

interface ApiEndPoint {

    @GET("/api/provinsi")
    fun getProvince() : Call<Response>

    //getNews
    @GET("/v2/top-headlines")
    fun getNews(
        @Query("q") q : String,
        @Query("category") category : String,
        @Query("apiKey") apiKey : String
    ) : Call<NewsResponse>

    //get data covid country
    @GET("/api/countries/{country}")
    fun getCountries(
      @Path("country") country : String
    ) : Call<CountryResponse>

//    get name country
    @GET("/api/countries/")
    fun getNameCountries() : Call<CountryNameResponse>

    //get global data
    @GET("/api")
    fun getGlobalData() : Call<CountryResponse>

    //get data global daily
    @GET("/api/daily/{daily}")
    fun getGlobalDataDaily(
        @Path("daily") daily : String
    ) : Call<List<CountryDailyResponse>>

    @GET("/v3/covid-19/historical/all?lastdays=lastday")
    fun getGlobalStatistic()

    @GET("/api/confirmed")
    fun getConfirmed() : Call<ArrayList<ProvinceResponse>>

    @GET("/v3/covid-19/all")
    fun getGlobalDataV2() : Call<GlobalResponse>

    @GET("/v3/covid-19/countries/{country}")
    fun getCountriesData(
        @Path("country") country: String
    ) : Call<GlobalResponse>
}