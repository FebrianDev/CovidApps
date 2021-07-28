package com.febrian.covidapp.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.MainActivity
import com.febrian.covidapp.databinding.ActivityBookmarkBinding
import com.febrian.covidapp.news.data.NewsDataResponse
import com.febrian.covidapp.news.room.NewsRoomDatabase

class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBookmarkBinding
    private val listNews : ArrayList<NewsDataResponse> = ArrayList<NewsDataResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mRoomDatabase = NewsRoomDatabase.getDatabase(this)
        val data = mRoomDatabase.newsDao().getAllNews()
        Log.d("TAG", data.size.toString())

        for(i in 0..data.size - 1) {
            val list = NewsDataResponse(
                title = data[i].title,
                url = data[i].url,
                urlToImage = data[i].urlToImage,
                publishedAt = data[i].publishedAt
            )
            Log.d("TAG", data[i].title.toString())
            listNews.add(list)
        }

        val adapter = BookmarkAdapter(listNews,activity = this@BookmarkActivity)
        binding.rvBookmark.setHasFixedSize(true)
        binding.rvBookmark.layoutManager = LinearLayoutManager(this)
        binding.rvBookmark.adapter = adapter

        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvBookmark.visibility = View.VISIBLE

        binding.back.setOnClickListener {
            finish()
        }

    }
}