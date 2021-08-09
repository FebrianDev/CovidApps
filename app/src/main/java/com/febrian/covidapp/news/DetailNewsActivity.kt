package com.febrian.covidapp.news

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebSettings.FORCE_DARK_OFF
import android.webkit.WebSettings.FORCE_DARK_ON
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ShareCompat
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.bumptech.glide.Glide
import com.febrian.covidapp.R
import com.febrian.covidapp.databinding.ActivityDetailNewsBinding
import com.febrian.covidapp.news.NewsAdapter.Companion.KEY_IMAGE_URL
import com.febrian.covidapp.news.NewsAdapter.Companion.KEY_NEWS
import com.febrian.covidapp.news.NewsAdapter.Companion.KEY_PUBLISHEDAT
import com.febrian.covidapp.news.NewsAdapter.Companion.KEY_TITLE
import com.febrian.covidapp.news.NewsAdapter.Companion.KEY_URL
import com.febrian.covidapp.news.data.NewsDataResponse
import com.febrian.covidapp.news.room.EntityNews
import com.febrian.covidapp.news.room.NewsRoomDatabase
import com.febrian.covidapp.news.utils.InternetConnection
import com.google.android.material.snackbar.Snackbar

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailNewsBinding

    @SuppressLint("UseCompatLoadingForDrawables", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loading.visibility = View.VISIBLE

        binding.back.setOnClickListener {
            finish()
        }

        val newsTitle = intent.getStringExtra(KEY_TITLE)
        val newsURL = intent.getStringExtra(KEY_URL)
        val newsImage = intent.getStringExtra(KEY_IMAGE_URL)
        val newsPublishedAt = intent.getStringExtra(KEY_PUBLISHEDAT)

        val action = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_open_anim)
        val items = AnimationUtils.loadAnimation(applicationContext, R.anim.from_bottom_anim)

        val actionClose = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_close_anim)
        val itemsClose = AnimationUtils.loadAnimation(applicationContext, R.anim.to_bottom_anim)

        var show = false

        val mRoomDatabase = NewsRoomDatabase.getDatabase(applicationContext).newsDao()

        binding.action.setOnClickListener {
            show = !show

            if(show){
                binding.bookmark.visibility = View.VISIBLE
                binding.share.visibility = View.VISIBLE
                binding.action.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_clear_24))
                binding.bookmark.startAnimation(items)
                binding.share.startAnimation(items)
                binding.action.startAnimation(action)
            }else{
                binding.bookmark.visibility = View.INVISIBLE
                binding.share.visibility = View.INVISIBLE
                binding.action.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_add_24))
                binding.bookmark.startAnimation(itemsClose)
                binding.share.startAnimation(itemsClose)
                binding.action.startAnimation(actionClose)
            }
        }

        if(mRoomDatabase.newsExist(newsTitle.toString())){
            Glide.with(applicationContext).load(R.drawable.ic_baseline_bookmark_24)
                .into(binding.bookmark)
        }else{
            Glide.with(applicationContext).load(R.drawable.ic_baseline_bookmark_border_24)
                .into(binding.bookmark)
        }

        binding.share.setOnClickListener {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder
                .from(this@DetailNewsActivity)
                .setType(mimeType)
                .setChooserTitle("Share")
                .setText(newsURL)
                .startChooser()
        }

        binding.bookmark.setOnClickListener {
            val bookmarkCheck = mRoomDatabase.newsExist(newsTitle.toString())

            if (!bookmarkCheck) {
                Snackbar.make(it, "News success add to bookmark", Snackbar.LENGTH_SHORT).show()
                Glide.with(applicationContext)
                    .load(R.drawable.ic_baseline_bookmark_24)
                    .into(binding.bookmark)
                val entityNews = EntityNews(
                    title = newsTitle.toString(),
                    url = newsURL.toString(),
                    urlToImage = newsImage.toString(),
                    publishedAt = newsPublishedAt.toString()
                )
                mRoomDatabase.insert(entityNews)
            } else {
                Snackbar.make(it, "News success delete from bookmark", Snackbar.LENGTH_SHORT).show()
                Glide.with(applicationContext).load(R.drawable.ic_baseline_bookmark_border_24)
                    .into(binding.bookmark)
                mRoomDatabase.delete(newsTitle.toString())
            }
        }

        main()
    }

    fun main(){
        val url: String = intent.getStringExtra(KEY_URL).toString()
        binding.web.settings.javaScriptEnabled = true
        binding.web.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                //   view?.loadUrl("javascript:alert('Web Berhasil Di Load')")
             //   binding.refreshLayout.visibility = View.GONE

                binding.loading.visibility = View.GONE
            }
        }

        binding.web.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                result?.confirm()
                return true
            }
        }
        binding.web.loadUrl(url)

    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!InternetConnection.isConnected(context)) {

                val builder = AlertDialog.Builder(applicationContext)
                val l_view = LayoutInflater.from(applicationContext).inflate(R.layout.alert_dialog_no_internet,null)
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

    override fun onResume() {
        super.onResume()
        val sharedPref =
            applicationContext.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val value = sharedPref.getString("KEY", "Follow By System")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            if (value == "Yes") {
//                WebSettingsCompat.setForceDark(binding.web.settings, FORCE_DARK_ON)
//            } else if (value == "No") {
//                WebSettingsCompat.setForceDark(binding.web.settings, FORCE_DARK_OFF)
//            } else {
//                val mode =
//                    applicationContext?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
//                if (mode == Configuration.UI_MODE_NIGHT_NO) {
//                    WebSettingsCompat.setForceDark(binding.web.settings, FORCE_DARK_OFF)
//                } else {
//                    WebSettingsCompat.setForceDark(binding.web.settings, FORCE_DARK_ON)
//                }
//            }
//        }
    }

    override fun onStart() {
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        applicationContext.registerReceiver(broadcastReceiver, intent)
        super.onStart()
    }

    override fun onStop() {
        applicationContext.unregisterReceiver(broadcastReceiver)
        super.onStop()
    }
}