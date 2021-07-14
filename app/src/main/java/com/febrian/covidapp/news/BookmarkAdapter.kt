package com.febrian.covidapp.news

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febrian.covidapp.databinding.ItemBookmarkBinding
import com.febrian.covidapp.databinding.ItemNewsBinding
import com.febrian.covidapp.news.data.NewsDataResponse
import com.febrian.covidapp.news.room.EntityNews
import com.febrian.covidapp.news.room.NewsRoomDatabase

class BookmarkAdapter(private var listNews: ArrayList<NewsDataResponse>) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

val myListNews: ArrayList<NewsDataResponse> = ArrayList()
    init {
        if (listNews != null && listNews.size > 0) {
            myListNews.clear();
            myListNews.addAll(listNews);
            notifyDataSetChanged();
        }
    }

    inner class ViewHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("CommitPrefEdits")
        fun bind(news: NewsDataResponse) {
            with(binding) {

                val mRoomDatabase = NewsRoomDatabase.getDatabase(itemView.context).newsDao()

                binding.titleNews.text = news.title.toString()
                binding.tglNews.text = news.publishedAt.toString()
                Glide.with(itemView.context).load(news.urlToImage).into(binding.imageNews)

                binding.titleNews.setOnClickListener {
                    val intent = Intent(itemView.context, DetailNewsActivity::class.java)
                    intent.putExtra(NewsAdapter.KEY_URL, news.url.toString())
                    itemView.context.startActivity(intent)
                }

                binding.bookmarkInBookmark.setOnClickListener {
                    val sharedPref = itemView.context.getSharedPreferences(NewsAdapter.PREFERENCENAME, Context.MODE_PRIVATE)
                    sharedPref.edit().remove(news.title).apply()
                    val entityNews = EntityNews(
                        title = news.title.toString(),
                        url = news.url.toString(),
                        urlToImage = news.urlToImage.toString(),
                        publishedAt = news.publishedAt.toString())
                    mRoomDatabase.delete(news.title.toString())
                    myListNews.removeAt(adapterPosition)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdapter.ViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkAdapter.ViewHolder, position: Int) {
        holder.bind(myListNews[position])
    }

    override fun getItemCount(): Int {
        return myListNews.size
    }
}