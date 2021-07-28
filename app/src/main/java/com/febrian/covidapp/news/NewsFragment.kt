package com.febrian.covidapp.news

import android.app.AlertDialog
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febrian.covidapp.R
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.FragmentNewsBinding
import com.febrian.covidapp.news.data.NewsDataResponse
import com.febrian.covidapp.news.data.NewsResponse
import com.febrian.covidapp.news.room.NewsRoomDatabase
import com.febrian.covidapp.news.utils.InternetConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {

    private lateinit var binding : FragmentNewsBinding
    private val listNews : ArrayList<NewsDataResponse> = ArrayList<NewsDataResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main()

        binding.refreshLayout.setOnRefreshListener {
            main()
        }
    }

    private fun main(){
        val q = "covid"
        showRecycleview(q)
        binding.searchView.setQuery("", false)
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
            val intent = Intent(view?.context, BookmarkActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showRecycleview(q:String){
        listNews.clear()

        val mRoomDatabase = NewsRoomDatabase.getDatabase(view?.context!!).newsDao()

        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.rvNews.visibility = View.GONE

        binding.shimmerFrameLayout1.startShimmer()
        binding.shimmerFrameLayout1.visibility = View.VISIBLE
        binding.dataHeadline.visibility = View.GONE

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

                    val adapter = NewsAdapter(listNews, activity = activity)

                        for (i in 0 until articles.size){
                            if(mRoomDatabase.newsExist(listNews[i].title.toString())){
                                adapter.setBookmark(i)
                            }
                        }

                        binding.rvNews.setHasFixedSize(true)
                        binding.rvNews.layoutManager = LinearLayoutManager(view?.context)
                        binding.rvNews.adapter = adapter
                        binding.refreshLayout.isRefreshing = false
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

                binding.refreshLayout.isRefreshing = false
            }

        })
    }

    private fun setHeadline(listNews : ArrayList<NewsDataResponse>) {
        if (listNews.size != 0) {
            binding.titleNews1.text = listNews[0].title.toString()
            binding.titleNews2.text = listNews[1].title.toString()
            binding.titleNews3.text = listNews[2].title.toString()

            view?.context?.applicationContext?.let {
                Glide.with(it)
                    .load(listNews[0].urlToImage)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(binding.imageNews1)
            }
            view?.context?.applicationContext?.let {
                Glide.with(it)
                    .load(listNews[1].urlToImage)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(binding.imageNews2)
            }
            view?.context?.applicationContext?.let {
                Glide.with(it)
                    .load(listNews[2].urlToImage)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(binding.imageNews3)
            }

            binding.headlineNews1.setOnClickListener {
                val intent = Intent(view?.context, DetailNewsActivity::class.java)
                intent.putExtra(NewsAdapter.KEY_URL, listNews[0].url.toString())
                startActivity(intent)
            }

            binding.headlineNews2.setOnClickListener {
                val intent = Intent(view?.context, DetailNewsActivity::class.java)
                intent.putExtra(NewsAdapter.KEY_URL, listNews[1].url.toString())
                startActivity(intent)
            }

            binding.headlineNews3.setOnClickListener {
                val intent = Intent(view?.context, DetailNewsActivity::class.java)
                intent.putExtra(NewsAdapter.KEY_URL, listNews[2].url.toString())
                startActivity(intent)
            }
        }
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!InternetConnection.isConnected(context)) {
                AlertDialog.Builder(context)
                    // Judul
                    .setTitle("Alert Dialog Title")
                    .setCancelable(false)
                    // Pesan yang di tamopilkan
                    .setMessage("Pesan Alert Dialog")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                        onReceive(context, intent)
                        main()
                    }).show()
            }
        }
    }

    override fun onStart() {
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        view?.context?.registerReceiver(broadcastReceiver, intent)
        super.onStart()
    }

    override fun onStop() {
        view?.context?.unregisterReceiver(broadcastReceiver)
        super.onStop()
    }
}