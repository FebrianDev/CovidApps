package com.febrian.covidapp.news

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febrian.covidapp.R
import com.febrian.covidapp.databinding.ItemNewsBinding
import com.febrian.covidapp.databinding.ItemNewsTrendingBinding
import com.febrian.covidapp.news.data.NewsDataResponse
import com.febrian.covidapp.news.room.EntityNews
import com.febrian.covidapp.news.room.NewsRoomDatabase
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

class NewsAdapter(private var listNews: ArrayList<NewsDataResponse>, private var activity: Activity?) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    val myListNews: ArrayList<NewsDataResponse> = ArrayList()
    val listPosition : ArrayList<Int> = ArrayList()

    fun setBookmark(position : Int){
        listPosition.add(position)
    }

    init {
        if (listNews != null && listNews.size > 0) {
            myListNews.clear();
            myListNews.addAll(listNews);
            notifyDataSetChanged();
        }
    }

    companion object {
        const val KEY_URL = "URL"
        const val KEY_TITLE = "TITLE"
        const val KEY_NEWS = "NEWS"
        const val KEY_PUBLISHEDAT = "PUBLISHED_AT"
        const val KEY_IMAGE_URL = "IMAGE_URL"
        const val PREFERENCENAME = "PREFERENCE_NAME"
        const val BOOKMARK = "BOOKMARK"
    }

    private var bookmarkCheck: Boolean = false
    private lateinit var sharedPref: SharedPreferences

    inner class ViewHolder(private val binding: ItemNewsTrendingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("CommitPrefEdits")
        fun bind(news: NewsDataResponse) {
            with(binding) {

                val mRoomDatabase = NewsRoomDatabase.getDatabase(itemView.context).newsDao()

                binding.titleNews.text = news.title.toString()
                binding.tglNews.text = news.publishedAt.toString()
                Glide.with(itemView.context)
                    .load(news.urlToImage)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_broken_image_24)
                    )
                    .into(binding.imageNews)

                binding.titleNews.setOnClickListener {
                    val intent = Intent(itemView.context, DetailNewsActivity::class.java)
                    intent.putExtra(KEY_TITLE, news.title)
                    intent.putExtra(KEY_URL, news.url)
                    intent.putExtra(KEY_IMAGE_URL, news.urlToImage)
                    intent.putExtra(KEY_PUBLISHEDAT, news.publishedAt)
                    itemView.context.startActivity(intent)
                }

                for (i in 0 until listPosition.size){
                    if(adapterPosition == listPosition[i]){
                        Glide.with(itemView.context).load(R.drawable.ic_baseline_bookmark_24)
                        .into(binding.bookmark)
                    }
                }

                binding.bookmark.setOnClickListener {
                    bookmarkCheck = mRoomDatabase.newsExist(news.title.toString())

                    if (!bookmarkCheck) {
                        Snackbar.make(itemView, "News success add to bookmark", Snackbar.LENGTH_SHORT).show()
                        Glide.with(itemView.context)
                            .load(R.drawable.ic_baseline_bookmark_24)
                            .into(binding.bookmark)
                        val entityNews = EntityNews(
                            title = news.title.toString(),
                            url = news.url.toString(),
                            urlToImage = news.urlToImage.toString(),
                            publishedAt = news.publishedAt.toString()
                        )
                        mRoomDatabase.insert(entityNews)
                    } else {
                        Snackbar.make(itemView, "News success delete from bookmark", Snackbar.LENGTH_SHORT).show()
                        Glide.with(itemView.context).load(R.drawable.ic_baseline_bookmark_border_24)
                            .into(binding.bookmark)
                        val entityNews = EntityNews(
                            title = news.title.toString(),
                            url = news.url.toString(),
                            urlToImage = news.urlToImage.toString(),
                            publishedAt = news.publishedAt.toString()
                        )
                        mRoomDatabase.delete(news.title.toString())
                    }
                }

                binding.share.setOnClickListener {
                    val mimeType = "text/plain"
                    ShareCompat.IntentBuilder
                        .from(activity!!)
                        .setType(mimeType)
                        .setChooserTitle("Share")
                        .setText(news.url)
                        .startChooser()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val binding =  ItemNewsTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.bind(myListNews[position])
    }

    override fun getItemCount(): Int {
        return myListNews.size
    }

}