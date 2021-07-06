package com.febrian.covidapp.news.data

data class NewsResponse(
    val status:String?,
    val totalResults:String?,
    val articles : ArrayList<NewsDataResponse>?
)
