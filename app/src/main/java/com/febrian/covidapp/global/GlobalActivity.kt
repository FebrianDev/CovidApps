package com.febrian.covidapp.global

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.setPadding
import com.febrian.covidapp.R
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.api.Constant
import com.febrian.covidapp.databinding.ActivityGlobalBinding
import com.febrian.covidapp.home.response.CountryResponse
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.ResponseHandlerInterface
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.*
import java.lang.Exception
import java.math.BigDecimal

class GlobalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGlobalBinding

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlobalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val response =
            ApiService.globalDataCovid.getGlobalData().enqueue(object : Callback<CountryResponse> {
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

        val client = AsyncHttpClient()
        val url = "https://disease.sh/v3/covid-19/historical/all?lastdays=30"
        client.get(url, object : AsyncHttpResponseHandler() {
            @SuppressLint("ResourceType")
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                try {
                    val responseObject = JSONObject(result)
                    val cases = responseObject.getJSONObject("cases")
                    val recoveredCase = responseObject.getJSONObject("recovered")
                    val deathCase = responseObject.getJSONObject("deaths")

                    val listCases = ArrayList<Entry>()
                    val listActive = ArrayList<Entry>()
                    val listRecovered = ArrayList<Entry>()
                    val listDeath = ArrayList<Entry>()

                    var j = 1f
                    for (i in 8 downTo 2) {
                        val getCases = cases.getInt(DateUtils.getDate(-i))
                        listCases.add(Entry(j, getCases.toFloat() / 1000))
                        val getRecovered = recoveredCase.getInt(DateUtils.getDate(-i))
                        listRecovered.add(Entry(j, getRecovered.toFloat() / 1000f))
                        val getDeath = deathCase.getInt(DateUtils.getDate(-i))
                        listDeath.add(Entry(j, getDeath.toFloat() / 1000))
                        val getActive = getCases - (getRecovered - getDeath)
                        listActive.add(Entry(j, getActive.toFloat() / 1000))
                        j++
                    }
                    blue()
                    setStatistic(listCases, R.color.blue_primary)
                    binding.totalCaseStatistic.setOnClickListener {
                        blue()
                        setStatistic(listCases, R.color.blue_primary)
                    }
                    binding.confirmedStatistic.setOnClickListener {
                        yellow()
                        setStatistic(listActive, R.color.yellow_primary)
                    }
                    binding.recoveredStatistic.setOnClickListener {
                        green()
                        setStatistic(listRecovered, R.color.green_custom)
                    }
                    binding.deceasedStatistic.setOnClickListener {
                        red()
                        setStatistic(listDeath, R.color.red_custom)
                    }

                } catch (e: Exception) {
                    Log.d("P", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {

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

        setBarChart(confirmed, recovered, deaths, totalCase)
    }

    private fun setBarChart(
        confirmed: BigDecimal,
        recovered: BigDecimal,
        death: BigDecimal,
        totalCase: BigDecimal
    ) {
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

    fun setStatistic(kasus : ArrayList<Entry>, color : Int){
        val kasusLineDataSet = LineDataSet(kasus, "Kasus")
        kasusLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        kasusLineDataSet.color = resources.getColor(R.color.bgColorPrimary)
        kasusLineDataSet.setCircleColor(resources.getColor(color))
        kasusLineDataSet.setDrawValues(false)
        kasusLineDataSet.setDrawFilled(false)
        kasusLineDataSet.disableDashedLine()
        kasusLineDataSet.setColors(resources.getColor(color))

        val lineChart: LineChart = findViewById(R.id.lineChart)

        lineChart.setTouchEnabled(false)
        lineChart.axisRight.isEnabled = false
        lineChart.setPinchZoom(false)
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawBorders(false)

        lineChart.xAxis.setDrawGridLines(false)
        lineChart.axisLeft.setDrawAxisLine(false)

        lineChart.xAxis.textColor = resources.getColor(R.color.colorPrimary)
        lineChart.xAxis.textSize = 12f

        lineChart.axisLeft.textColor = resources.getColor(R.color.colorPrimary)
        lineChart.axisLeft.textSize = 12f

        //Setup Legend
        val legend = lineChart.legend
        legend.isEnabled = false

        val date = ArrayList<String>();
        for (i in 8 downTo 1) {
            date.add(DateUtils.getDateStatistic(-i))
        }

        val tanggal = AxisDateFormatter(date.toArray(arrayOfNulls<String>(date.size)))
        lineChart.xAxis?.valueFormatter = tanggal;

        lineChart.description.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.spaceMax = 0.5f

        lineChart.data = LineData(kasusLineDataSet)
        lineChart.animateY(100, Easing.Linear)
    }

    class AxisDateFormatter(private val mValues: Array<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return if (value >= 0) {
                if (mValues.size > value.toInt()) {
                    mValues[value.toInt()]
                } else ""
            } else {
                ""
            }
        }
    }

    fun blue(){
        setBlue()
        resetYellow()
        resetGreen()
        resetRed()
    }

    fun yellow(){
        setYellow()
        resetGreen()
        resetRed()
        resetBlue()
    }

    fun green(){
        setGreen()
        resetRed()
        resetYellow()
        resetBlue()
    }

    fun red(){
        setRed()
        resetBlue()
        resetYellow()
        resetGreen()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setBlue() {
        binding.circleBlue.visibility = View.GONE
        binding.textBlue.background = resources.getDrawable(R.drawable.bg_weekly_blue)
        binding.textBlue.setTextColor(resources.getColor(R.color.white))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun resetBlue() {
        binding.circleBlue.visibility = View.VISIBLE
        binding.textBlue.background = resources.getDrawable(android.R.color.transparent)
        binding.textBlue.setTextColor(resources.getColor(R.color.colorPrimary))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setYellow() {
        binding.icYellow.visibility = View.GONE
        binding.textYellow.background = resources.getDrawable(R.drawable.bg_weekly_yellow)
        binding.textYellow.setTextColor(resources.getColor(R.color.white))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun resetYellow() {
        binding.icYellow.visibility = View.VISIBLE
        binding.textYellow.background = resources.getDrawable(android.R.color.transparent)
        binding.textYellow.setTextColor(resources.getColor(R.color.colorPrimary))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setGreen() {
        binding.icGreen.visibility = View.GONE
        binding.textGreen.background = resources.getDrawable(R.drawable.bg_weekly_green)
        binding.textGreen.setTextColor(resources.getColor(R.color.white))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun resetGreen() {
        binding.icGreen.visibility = View.VISIBLE
        binding.textGreen.background = resources.getDrawable(android.R.color.transparent)
        binding.textGreen.setTextColor(resources.getColor(R.color.colorPrimary))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setRed() {
        binding.icRed.visibility = View.GONE
        binding.textRed.background = resources.getDrawable(R.drawable.bg_weekly_red)
        binding.textRed.setTextColor(resources.getColor(R.color.white))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun resetRed() {
        binding.icRed.visibility = View.VISIBLE
        binding.textRed.background = resources.getDrawable(android.R.color.transparent)
        binding.textRed.setTextColor(resources.getColor(R.color.colorPrimary))
    }
}