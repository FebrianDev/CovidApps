package com.febrian.covidapp.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.febrian.covidapp.databinding.ActivityOnboardingBinding
import com.febrian.covidapp.screen.oboarding.ViewPagerAdapter

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter

    }
}