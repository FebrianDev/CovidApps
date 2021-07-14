package com.febrian.covidapp.news.room

import androidx.room.*

@Dao
interface NewsDao {
    @Insert
    fun insert(news : EntityNews)

    @Query("DELETE from news where title = :myTitle")
    fun delete(myTitle : String)

    @Query("SELECT * from news order by id asc")
    fun getAllNews():List<EntityNews>

    @Query("SELECT * from news where id = :myId ")
    fun getNewsById(myId : Int) : EntityNews

    @Query("SELECT * from news where title = :myTitle")
    fun getNewsByTitle(myTitle : String) : EntityNews

    @Query("SELECT EXISTS(SELECT * from news where title = :myTitle)")
    fun newsExist(myTitle: String) : Boolean
}