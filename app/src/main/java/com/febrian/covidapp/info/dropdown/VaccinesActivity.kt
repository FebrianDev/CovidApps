package com.febrian.covidapp.info.dropdown

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.databinding.ActivityVaccinesBinding
import com.febrian.covidapp.info.DropdownInfoAdapter
import com.febrian.covidapp.info.data.DataVaccines

class VaccinesActivity : AppCompatActivity() {

    private lateinit var binding : ActivityVaccinesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaccinesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = DropdownInfoAdapter(DataVaccines.getVaccines(applicationContext), applicationContext)

        binding.back.setOnClickListener { finish() }
    }
}