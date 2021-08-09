package com.febrian.covidapp.info.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.databinding.ActivityPreventionBinding
import com.febrian.covidapp.info.ListInfoAdapter
import com.febrian.covidapp.info.data.DataPrevention
import com.febrian.covidapp.info.data.DataSymptoms

class PreventionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPreventionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreventionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = ListInfoAdapter(DataPrevention.getPrevention(applicationContext))

        binding.back.setOnClickListener { finish() }
    }
}