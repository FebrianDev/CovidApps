package com.febrian.covidapp.info.dropdown

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.databinding.ActivityPeopleAtRiskBinding
import com.febrian.covidapp.info.DropdownInfoAdapter
import com.febrian.covidapp.info.data.DataPeopeAtRisk

class PeopleAtRiskActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPeopleAtRiskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleAtRiskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = DropdownInfoAdapter(DataPeopeAtRisk.getPeopleRisk(applicationContext), applicationContext)

        binding.back.setOnClickListener { finish() }
    }
}