package com.febrian.covidapp.info.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.databinding.ActivitySymptomsBinding
import com.febrian.covidapp.info.DropdownInfoAdapter
import com.febrian.covidapp.info.ListInfoAdapter
import com.febrian.covidapp.info.data.DataCovidTest
import com.febrian.covidapp.info.data.DataSymptoms

class SymptomsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySymptomsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = ListInfoAdapter(DataSymptoms.getSymptoms())

        binding.back.setOnClickListener { finish() }
    }
}