package com.febrian.covidapp.info.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.databinding.ActivityExerciseBinding
import com.febrian.covidapp.info.ListInfoAdapter
import com.febrian.covidapp.info.data.DataExercise
import com.febrian.covidapp.info.data.DataPrevention

class ExerciseActivity : AppCompatActivity() {

    private lateinit var binding : ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = ListInfoAdapter(DataExercise.getExercise())

        binding.back.setOnClickListener { finish() }
    }
}