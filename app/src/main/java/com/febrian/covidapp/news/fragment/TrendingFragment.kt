package com.febrian.covidapp.news.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.FragmentTrendingBinding
import com.febrian.covidapp.news.NewsAdapter
import com.febrian.covidapp.news.data.NewsDataResponse
import com.febrian.covidapp.news.data.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrendingFragment : Fragment() {

    val TAG = "TRENDING"

    private val listNews : ArrayList<NewsDataResponse> = ArrayList<NewsDataResponse>()
    private lateinit var binding : FragmentTrendingBinding

    private lateinit var c : Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrendingBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        c = view.context

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

    }

    private fun showRecycleview(q:String){
        listNews.clear()
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        Log.d("TAG", "Resume")
        Toast.makeText(c, "Resume", Toast.LENGTH_LONG).show()
        Log.d(TAG, listNews.size.toString())
        binding.shimmerFrameLayout.startShimmer()
        ApiService.newsCovid.getNews(
            q,
            "health",
            "16ad27417fef4cd3a674c3b6339af476"
        ).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                Log.d(TAG, response.body()?.articles?.size.toString())

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

                        val adapter = NewsAdapter(listNews, activity)
                        binding.rvNews.setHasFixedSize(true)
                        binding.rvNews.layoutManager = LinearLayoutManager(c)
                        binding.rvNews.adapter = adapter
                    }
                }

            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {

            }

        })
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "Start")
        Toast.makeText(c, "Start", Toast.LENGTH_LONG).show()
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
    }


    override fun onStop() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        super.onStop()
Toast.makeText(c, "STOP", Toast.LENGTH_LONG).show()
        Log.d("TAG", "STOP")
    }

}