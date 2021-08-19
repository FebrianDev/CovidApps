package com.febrian.covidapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.febrian.covidapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    val TAG = "Main Activity"
    private lateinit var binding: ActivityMainBinding

    companion object{
        const val KEY_LOG = "KEY_LOG"
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.bottomNavigation.setupWithNavController(navController)

        val sharedPref = applicationContext.getSharedPreferences(KEY_LOG, Context.MODE_PRIVATE)
        sharedPref.edit().putString(KEY_LOG, KEY_LOG).apply()

    }

}