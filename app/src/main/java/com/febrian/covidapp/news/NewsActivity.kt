package com.febrian.covidapp.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.covidapp.R
import com.febrian.covidapp.SectionPagerAdapter
import com.febrian.covidapp.databinding.ActivityNewsBinding
import com.google.android.material.tabs.TabLayoutMediator

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding

    private val TAB_TITLES = arrayOf("Terbaru", "Trending","Favorit")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }
}