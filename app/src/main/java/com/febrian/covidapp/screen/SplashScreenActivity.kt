package com.febrian.covidapp.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.covidapp.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        GlobalScope.launch {
            delay(1000)

        }

    }
}