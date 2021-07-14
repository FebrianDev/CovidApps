package com.febrian.covidapp.news

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.news.fragment.SectionPagerAdapter
import com.febrian.covidapp.databinding.ActivityNewsBinding
import com.febrian.covidapp.news.data.NewsDataResponse
import com.febrian.covidapp.news.data.NewsResponse
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private val listNews : ArrayList<NewsDataResponse> = ArrayList<NewsDataResponse>()
    private val TAB_TITLES = arrayOf("Trending","Bookmark")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val q = "covid"
        showRecycleview(q)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showRecycleview(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {

                showRecycleview(newText)
                return true
            }
        })

        binding.btnBookmarks.setOnClickListener {
            val intent = Intent(applicationContext, BookmarkActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showRecycleview(q:String){
        listNews.clear()
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        Log.d("TAG", "Resume")
        Toast.makeText(applicationContext, "Resume", Toast.LENGTH_LONG).show()
        Log.d("TAG", listNews.size.toString())
        binding.shimmerFrameLayout.startShimmer()
        ApiService.newsCovid.getNews(
            q,
            "health",
            "16ad27417fef4cd3a674c3b6339af476"
        ).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                Log.d("TAG", response.body()?.articles?.size.toString())

                if(response.isSuccessful){

                    binding.shimmerFrameLayout.stopShimmer()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    binding.rvNews.visibility = View.VISIBLE

                    val articles = response.body()?.articles
                    if(articles != null){
                        for(i in 0 until articles.size - 1 ) {
                            val data: NewsDataResponse = NewsDataResponse(
                                articles[i].title?.split(" - ")?.get(0).toString(),
                                articles[i].url,
                                articles[i].urlToImage,
                                articles[i].publishedAt?.split("T")?.get(0).toString())
                            listNews.add(data)
                        }

                        val adapter = NewsAdapter(listNews, activity = this@NewsActivity)
                        binding.rvNews.setHasFixedSize(true)
                        binding.rvNews.layoutManager = LinearLayoutManager(applicationContext)
                        binding.rvNews.adapter = adapter
                    }
                }

            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {

            }

        })
    }

}