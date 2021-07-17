package com.febrian.covidapp.global

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.febrian.covidapp.R
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.ActivityGlobalBinding
import com.febrian.covidapp.home.response.CountryResponse
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import java.math.BigDecimal

class GlobalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGlobalBinding

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlobalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val response = ApiService.globalDataCovid.getGlobalData().enqueue(object:Callback<CountryResponse>{
            override fun onResponse(
                call: Call<CountryResponse>,
                response: Response<CountryResponse>
            ) {
                if (response.isSuccessful) {

                    val body = response.body()
                    if (body != null) {
                        setGlobalData(body)
                    }
                }
            }

            override fun onFailure(call: Call<CountryResponse>, t: Throwable) {

            }

        })

    }

    @SuppressLint("SetTextI18n")
    private fun setGlobalData(body: CountryResponse) {
        val totalCase = body.confirmed?.value!!.toBigDecimal()
        val recovered = body.recovered?.value!!.toBigDecimal()
        val deaths = body.deaths?.value!!.toBigDecimal()
        val confirmed = totalCase - (recovered + deaths)

        binding.confirmed.text = confirmed.toString()
        binding.recovered.text = recovered.toString()
        binding.deceased.text = deaths.toString()

        setBarChart(confirmed, recovered, deaths,totalCase)
    }

    private fun setBarChart(confirmed:BigDecimal, recovered:BigDecimal, death:BigDecimal, totalCase:BigDecimal) {
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()

        listPie.add(PieEntry(confirmed.toFloat()))
        listColors.add(resources.getColor(R.color.red_custom))
        listPie.add(PieEntry(recovered.toFloat()))
        listColors.add(resources.getColor(R.color.green_custom))
        listPie.add(PieEntry(death.toFloat()))
        listColors.add(resources.getColor(R.color.gray_custom))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(0f)
        pieData.setValueTextColor(resources.getColor(android.R.color.transparent))
        binding.pieChart.data = pieData

        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        //set text center
        binding.pieChart.centerText = "Total Case\n${totalCase}"
        binding.pieChart.setCenterTextColor(R.color.colorPrimary)
        binding.pieChart.setCenterTextSize(12f)

        binding.pieChart.legend.isEnabled = false // hide tags labels

        binding.pieChart.setDrawEntryLabels(false)
        binding.pieChart.description.isEnabled = false
    }
}