package com.febrian.covidapp.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.covidapp.R
import com.febrian.covidapp.databinding.ActivityOnboardingBinding
import com.febrian.covidapp.screen.oboarding.ViewPagerAdapter

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager)

    }
}