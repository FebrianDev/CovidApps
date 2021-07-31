package com.febrian.covidapp.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febrian.covidapp.R
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.ActivityNewsBinding
import com.febrian.covidapp.news.data.NewsDataResponse
import com.febrian.covidapp.news.data.NewsResponse
import com.febrian.covidapp.news.room.NewsRoomDatabase
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
            finish()
        }
    }

    private fun showRecycleview(q:String){
        listNews.clear()

        val mRoomDatabase = NewsRoomDatabase.getDatabase(applicationContext).newsDao()

        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE

        binding.shimmerFrameLayout1.startShimmer()
        binding.shimmerFrameLayout1.visibility = View.VISIBLE

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

                    binding.shimmerFrameLayout1.stopShimmer()
                    binding.shimmerFrameLayout1.visibility = View.GONE
                    binding.dataHeadline.visibility = View.VISIBLE

                    val articles = response.body()?.articles
                    if(articles != null){
                        for(i in 0 until articles.size ) {
                            val data: NewsDataResponse = NewsDataResponse(
                                articles[i].title?.split(" - ")?.get(0).toString(),
                                articles[i].url,
                                articles[i].urlToImage,
                                articles[i].publishedAt?.split("T")?.get(0).toString())
                            listNews.add(data)
                        }

                        setHeadline(listNews)


                        val adapter = NewsAdapter(listNews, activity = this@NewsActivity)

                        for (i in 0 until articles.size){
                            if(mRoomDatabase.newsExist(listNews[i].title.toString())){
                                adapter.setBookmark(i)
                            }
                        }

                        binding.rvNews.setHasFixedSize(true)
                        binding.rvNews.layoutManager = LinearLayoutManager(applicationContext)
                        binding.rvNews.adapter = adapter
                    }
                }

            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {

                binding.shimmerFrameLayout.stopShimmer()
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.rvNews.visibility = View.VISIBLE

                binding.shimmerFrameLayout1.stopShimmer()
                binding.shimmerFrameLayout1.visibility = View.GONE
                binding.dataHeadline.visibility = View.VISIBLE
            }

        })
    }

    private fun setHeadline(listNews : ArrayList<NewsDataResponse>){
        binding.titleNews1.text = listNews[0].title.toString()
        binding.titleNews2.text = listNews[1].title.toString()
        binding.titleNews3.text = listNews[2].title.toString()

        Glide.with(applicationContext)
            .load(listNews[0].urlToImage)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
            )
            .into(binding.imageNews1)
        Glide.with(applicationContext)
            .load(listNews[1].urlToImage)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
            )
            .into(binding.imageNews2)
        Glide.with(applicationContext)
            .load(listNews[2].urlToImage)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
            )
            .into(binding.imageNews3)
    }

}