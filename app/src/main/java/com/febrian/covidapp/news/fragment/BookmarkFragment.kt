package com.febrian.covidapp.news.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.R
import com.febrian.covidapp.databinding.FragmentBookmarkBinding
import com.febrian.covidapp.news.BookmarkAdapter
import com.febrian.covidapp.news.NewsAdapter
import com.febrian.covidapp.news.data.NewsDataResponse
import com.febrian.covidapp.news.room.EntityNews
import com.febrian.covidapp.news.room.NewsRoomDatabase

class BookmarkFragment : Fragment() {

    private val listNews : ArrayList<NewsDataResponse> = ArrayList<NewsDataResponse>()
    private lateinit var binding : FragmentBookmarkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookmarkBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    private lateinit var c : Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        c = view.context

    }

    override fun onResume() {
        super.onResume()
        val mRoomDatabase = NewsRoomDatabase.getDatabase(c)
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

        val adapter = BookmarkAdapter(listNews,activity)
        binding.rvBookmark.setHasFixedSize(true)
        binding.rvBookmark.layoutManager = LinearLayoutManager(c)
        binding.rvBookmark.adapter = adapter

        listNews.clear()
    }
}