package com.febrian.covidapp.info.dropdown

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.databinding.ActivityCovidTestBinding
import com.febrian.covidapp.info.DropdownInfoAdapter
import com.febrian.covidapp.info.data.DataCovidTest

class CovidTestActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCovidTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCovidTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = DropdownInfoAdapter(DataCovidTest.getCovidTest(applicationContext), applicationContext)

        binding.back.setOnClickListener { finish() }
    }
}