package com.febrian.covidapp.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.febrian.covidapp.R
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.ActivityHomeBinding
import com.febrian.covidapp.home.response.CountryNameResponse
import com.febrian.covidapp.home.response.CountryResponse
import com.febrian.covidapp.home.response.DetailCountryNameResponse
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    val TAG = "Home Activity"
    private lateinit var binding: ActivityHomeBinding
    val listCountries: ArrayList<String> = ArrayList<String>()

    val barList: ArrayList<PieEntry> = ArrayList<PieEntry>()

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.IO) {
            val task = listOf(
                async {
                    ApiService.globalDataCovid.getNameCountries()
                        .enqueue(object : Callback<CountryNameResponse> {
                            override fun onResponse(
                                call: Call<CountryNameResponse>,
                                response: retrofit2.Response<CountryNameResponse>
                            ) {
                                if (response.isSuccessful) {

                                    val data = response.body()?.countries

                                    if (data != null) {
                                        setListCountries(data)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<CountryNameResponse>, t: Throwable) {
                                Log.d(TAG, t.message.toString())
                            }
                        })
                }
            )

            task.awaitAll()
        }

        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)

        binding.textTgl.text = currentDate

//        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
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

    private fun setBarChart(positif: Float, sembuh: Float, meninggal: Float, dirawat: Float) {
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()
        listPie.add(PieEntry(positif))
        listColors.add(resources.getColor(R.color.red_custom))
        listPie.add(PieEntry(sembuh))
        listColors.add(resources.getColor(R.color.green_custom))
        listPie.add(PieEntry(meninggal))
        listColors.add(resources.getColor(R.color.brown_custom))
        listPie.add(PieEntry(dirawat))
        listColors.add(resources.getColor(R.color.blue_custom))

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

    private fun setListCountries(data: ArrayList<DetailCountryNameResponse>) {
        val size = data.size - 1

        for (i in 0..size) {
            val country = data[i].name.toString()
            listCountries.add(country)
        }

        val listAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listCountries)
        binding.spinner.adapter = listAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val country = parent?.getItemAtPosition(position).toString()
                binding.negara.text = country

                ApiService.globalDataCovid.getCountries(country)
                    .enqueue(object : Callback<CountryResponse> {
                        override fun onResponse(
                            call: Call<CountryResponse>,
                            response: Response<CountryResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.d(TAG, response.body()?.confirmed?.value.toString())

                                val dataPos = response.body()

                                val dirawat = dataPos?.confirmed?.value!!.toInt() -
                                        (dataPos.recovered?.value!!.toInt() + dataPos.deaths?.value!!.toInt())

                                binding.totalKasus.text = (dataPos.confirmed.value.toInt() +
                                        dataPos.recovered.value.toInt() +
                                        dataPos.deaths.value.toInt()).toString()

                                binding.positif.text = dataPos.confirmed.value.toString()
                                binding.sembuh.text = dataPos.recovered.value.toString()
                                binding.meninggal.text = dataPos.deaths.value.toString()
                                binding.aktif.text = dirawat.toString()

                                setBarChart(
                                    dataPos.confirmed.value.toFloat(),
                                    dataPos.recovered.value.toFloat(),
                                    dataPos.deaths.value.toFloat(),
                                    dirawat.toFloat()
                                )
                            }
                        }

                        override fun onFailure(call: Call<CountryResponse>, t: Throwable) {

                        }

                    })

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}