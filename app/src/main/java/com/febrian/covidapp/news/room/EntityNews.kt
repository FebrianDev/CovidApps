package com.febrian.covidapp.news.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="news")
data class EntityNews(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "title")
    val title : String?,
    @ColumnInfo(name = "url")
    val url:String?,
    @ColumnInfo(name = "urlToImage")
    val urlToImage:String?,
    @ColumnInfo(name = "publishedAt")
    val publishedAt:String?
)