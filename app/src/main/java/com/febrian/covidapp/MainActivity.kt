package com.febrian.covidapp

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.ActivityMainBinding
import com.febrian.covidapp.home.DataResponse
import com.febrian.covidapp.home.Response
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import java.text.DateFormat

import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    val TAG = "Main Activity"
    private lateinit var binding: ActivityMainBinding
    val listProvinsi: ArrayList<String> = ArrayList<String>()

    val barList : ArrayList<PieEntry> = ArrayList<PieEntry>()

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.IO) {
            ApiService.dataCovid.getProvince().enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, response.body()?.data?.get(0)?.provinsi.toString())
                        val data = response.body()?.data

                        if (data != null) {
                            setListProvinsi(data)
                        }
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                }
            })
        }

        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)

        binding.update.text = currentDate

//        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {item ->
//
//            when(item.itemId) {
//                R.id.page_1 -> {
//                    // Respond to navigation item 1 click
//                    true
//                }
//                R.id.page_2 -> {
//                    // Respond to navigation item 2 click
//                    true
//                }
//                else -> false
//            }
//        })
    }

    private fun setBarChart(positif:Float, sembuh:Float, meninggal:Float) {
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(positif))
        listColors.add(resources.getColor(R.color.red_custom))
        listPie.add(PieEntry(sembuh))
        listColors.add(resources.getColor(R.color.green_custom))
        listPie.add(PieEntry(meninggal))
        listColors.add(resources.getColor(R.color.gray_custom))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(0f)
        pieData.setValueTextColor(resources.getColor(android.R.color.transparent))
        binding.chart.data = pieData

        binding.chart.setUsePercentValues(true)
        binding.chart.isDrawHoleEnabled = false
        binding.chart.description.isEnabled = false
        binding.chart.setTouchEnabled(false)
        binding.chart.setDrawEntryLabels(false)
        binding.chart.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun setListProvinsi(data: List<DataResponse>) {
        val size = data.size - 1

        for (i in 0..size) {
            val provinsi = data[i].provinsi.toString()
            listProvinsi.add(provinsi)
        }

        val listAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listProvinsi)
        binding.spinner.adapter = listAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.provinsi.text = parent?.getItemAtPosition(position).toString()

                if (parent?.getItemAtPosition(position)
                        .toString() == data[position].provinsi.toString()
                ) {
                    val dataPos = data[position]
                    binding.totalKasus.text =
                        (dataPos.kasusPosi!! + dataPos.kasusSemb!! + dataPos.kasusMeni!!).toString()
                    binding.positif.text = dataPos.kasusPosi.toString()
                    binding.sembuh.text = dataPos.kasusSemb.toString()
                    binding.meninggal.text = dataPos.kasusMeni.toString()
                    setBarChart(dataPos.kasusPosi.toFloat(), dataPos.kasusSemb.toFloat(), dataPos.kasusMeni.toFloat())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

}