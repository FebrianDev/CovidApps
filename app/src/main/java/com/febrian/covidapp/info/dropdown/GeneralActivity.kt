package com.febrian.covidapp.info.dropdown

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.febrian.covidapp.databinding.ActivityGeneralBinding
import com.febrian.covidapp.info.DropdownInfoAdapter
import com.febrian.covidapp.info.data.DataGeneral


class GeneralActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeneralBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = DropdownInfoAdapter(DataGeneral.getDataGeneral())

        binding.back.setOnClickListener {
            finish()
        }
    }
}