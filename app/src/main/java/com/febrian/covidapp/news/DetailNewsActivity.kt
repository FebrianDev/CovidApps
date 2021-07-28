package com.febrian.covidapp.news

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.covidapp.databinding.ActivityDetailNewsBinding
import com.febrian.covidapp.news.NewsAdapter.Companion.KEY_URL

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isConnect()) {
            val url: String = intent.getStringExtra(KEY_URL).toString()
            binding.web.settings.javaScriptEnabled = true
            binding.web.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                 //   view?.loadUrl("javascript:alert('Web Berhasil Di Load')")
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
        }else{
            Toast.makeText(applicationContext, "No Internet Connection!", Toast.LENGTH_LONG).show()
        }
    }

    private fun isConnect() : Boolean{
        val connect : ConnectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return connect.activeNetworkInfo != null && connect.activeNetworkInfo!!.isConnected
    }
    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // app icon in action bar clicked; go home
                val intent = Intent(this, NewsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}