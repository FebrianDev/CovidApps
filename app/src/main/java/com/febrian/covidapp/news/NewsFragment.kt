package com.febrian.covidapp.news

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
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
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.ReportPolicy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class NewsFragment : Fragment() {

    private lateinit var binding : FragmentNewsBinding
    private val listNews : ArrayList<NewsDataResponse> = ArrayList<NewsDataResponse>()
    
    private lateinit var c : Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        
        c = context 
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HiAnalyticsTools.enableLog()
        val instance: HiAnalyticsInstance = HiAnalytics.getInstance(c)
        instance.setAnalyticsEnabled(true)
        instance.setUserProfile("userKey", "value")
        instance.setAutoCollectionEnabled(true)
        instance.regHmsSvcEvent()
        val launch: ReportPolicy = ReportPolicy.ON_APP_LAUNCH_POLICY
        val report: MutableSet<ReportPolicy> = HashSet<ReportPolicy>()

        report.add(launch)

        instance.setReportPolicies(report)

        val bundle = Bundle()
        bundle.putString("News", "News")
        instance.onEvent("News", bundle)

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
            val intent = Intent(c, BookmarkActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showRecycleview(q:String){
        listNews.clear()

        val mRoomDatabase = NewsRoomDatabase.getDatabase(c).newsDao()

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
                        binding.rvNews.layoutManager = LinearLayoutManager(c)
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

//                Toast.makeText(c, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setHeadline(listNews : ArrayList<NewsDataResponse>) {
        if (listNews.size != 0) {
            binding.titleNews1.text = listNews[0].title.toString()
            binding.titleNews2.text = listNews[1].title.toString()
            binding.titleNews3.text = listNews[2].title.toString()

            c?.applicationContext?.let {
                Glide.with(it)
                    .load(listNews[0].urlToImage)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(binding.imageNews1)
            }
            c?.applicationContext?.let {
                Glide.with(it)
                    .load(listNews[1].urlToImage)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(binding.imageNews2)
            }
            c?.applicationContext?.let {
                Glide.with(it)
                    .load(listNews[2].urlToImage)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(binding.imageNews3)
            }

            binding.headlineNews1.setOnClickListener {
                val intent = Intent(c, DetailNewsActivity::class.java)
                intent.putExtra(NewsAdapter.KEY_URL, listNews[0].url.toString())
                startActivity(intent)
            }

            binding.headlineNews2.setOnClickListener {
                val intent = Intent(c, DetailNewsActivity::class.java)
                intent.putExtra(NewsAdapter.KEY_URL, listNews[1].url.toString())
                startActivity(intent)
            }

            binding.headlineNews3.setOnClickListener {
                val intent = Intent(c, DetailNewsActivity::class.java)
                intent.putExtra(NewsAdapter.KEY_URL, listNews[2].url.toString())
                startActivity(intent)
            }
        }
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!InternetConnection.isConnected(context)) {

                val builder = AlertDialog.Builder(c)
                val l_view = LayoutInflater.from(c).inflate(R.layout.alert_dialog_no_internet,null)
                builder.setView(l_view)

                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                val btnRetry = l_view.findViewById<AppCompatButton>(R.id.btn_retry)
                btnRetry.setOnClickListener{
                    dialog.dismiss()
                    onReceive(context,intent)
                    main()
                }
            }
        }
    }

    override fun onStart() {
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        c.registerReceiver(broadcastReceiver, intent)
        super.onStart()
    }

    override fun onStop() {
        c.unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        main()
    }
}