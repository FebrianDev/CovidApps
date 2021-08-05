package com.febrian.covidapp.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.febrian.covidapp.MainActivity
import com.febrian.covidapp.MainActivity.Companion.KEY_LOG
import com.febrian.covidapp.R
import com.febrian.covidapp.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = applicationContext.getSharedPreferences(MainActivity.KEY_LOG, Context.MODE_PRIVATE)
        val value = sharedPref.getString(KEY_LOG, "")
        GlobalScope.launch {
            delay(1500)

            if(value == KEY_LOG) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(applicationContext, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        val animLogo = AnimationUtils.loadAnimation(applicationContext, R.anim.logo_anim)
        val animText = AnimationUtils.loadAnimation(applicationContext, R.anim.text_logo)
        binding.imgLogo.startAnimation(animLogo)
        binding.text.startAnimation(animText)
        binding.textView.startAnimation(animText)

        val mode =
            application.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                Glide.with(applicationContext).load(R.drawable.logo_white).into(binding.imgLogo)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                Glide.with(applicationContext).load(R.drawable.logo_black).into(binding.imgLogo)
            }
        }
    }
}