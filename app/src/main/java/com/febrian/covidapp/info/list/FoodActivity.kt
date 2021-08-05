package com.febrian.covidapp.info.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.databinding.ActivityFoodBinding
import com.febrian.covidapp.info.ListInfoAdapter
import com.febrian.covidapp.info.data.DataExercise
import com.febrian.covidapp.info.data.DataFood

class FoodActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = ListInfoAdapter(DataFood.getFood())

        binding.back.setOnClickListener { finish() }
    }
}