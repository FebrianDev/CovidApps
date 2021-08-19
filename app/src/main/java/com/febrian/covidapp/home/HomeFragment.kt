package com.febrian.covidapp.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.febrian.covidapp.MainActivity
import com.febrian.covidapp.R
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.FragmentHomeBinding
import com.febrian.covidapp.global.DateUtils
import com.febrian.covidapp.global.GlobalResponse
import com.febrian.covidapp.news.utils.InternetConnection
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.ReportPolicy
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.DelicateCoroutinesApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "covid channel"
        const val TAG = "Home Activity"
    }

    private lateinit var c : Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        
        c = context
    }

    val listConfirm: MutableList<Int> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HiAnalyticsTools.enableLog()
        val instance: HiAnalyticsInstance = HiAnalytics.getInstance(view.context)
        instance.setAnalyticsEnabled(true)
        instance.setUserProfile("userKey", "value")
        instance.setAutoCollectionEnabled(true)
        instance.regHmsSvcEvent()
        val launch: ReportPolicy = ReportPolicy.ON_APP_LAUNCH_POLICY
        val report: MutableSet<ReportPolicy> = HashSet<ReportPolicy>()

        report.add(launch)

        instance.setReportPolicies(report)

        val bundle = Bundle()
        bundle.putString("Home", "Home")
        instance.onEvent("Home", bundle)

        main()
        binding.swiperefresh.setOnRefreshListener {
            binding.searchView.setQuery("", false)
            main()
        }
    }

    internal fun main() {

        var location = c.resources?.configuration?.locale?.displayCountry

        if (location == "" || location == null)
            location = "Indonesia"

        showStatistic(location.toString())
        showTotalCase(location.toString())

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        binding.tgl.text = currentDate
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showStatistic(query)
                showTotalCase(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    @DelicateCoroutinesApi
    private fun showStatistic(query: String) {
        ApiService.newService.getCountriesData(query)
            .enqueue(object : Callback<GlobalResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<GlobalResponse>,
                    response: Response<GlobalResponse>
                ) {
                    if (response.isSuccessful) {

                        try {
                            val body = response.body()
                            if (body != null) {
                                val totalCase = body.cases!!.toBigDecimal()
                                val recovered = body.recovered!!.toBigDecimal()
                                val deaths = body.deaths!!.toBigDecimal()
                                val confirmed = totalCase - (recovered + deaths)
                                val updated = body.updated

                                binding.confirmedValue.text =
                                    NumberFormat.getInstance().format(confirmed).toString()
                                binding.recoveredValue.text =
                                    NumberFormat.getInstance().format(recovered).toString()
                                binding.deathValue.text =
                                    NumberFormat.getInstance().format(deaths).toString()
                                binding.totalCaseValue.text =
                                    NumberFormat.getInstance().format(totalCase).toString()
                                setBarChart(confirmed, recovered, deaths, totalCase, query)
                            }
                            binding.swiperefresh.isRefreshing = false
                        } catch (e: Exception) {
                            binding.swiperefresh.isRefreshing = false
                            Toast.makeText(c, e.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                    binding.swiperefresh.isRefreshing = false
                    Toast.makeText(c.applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
                }

            })

    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!InternetConnection.isConnected(context)) {

                val builder = AlertDialog.Builder(c)
                val l_view = LayoutInflater.from(c)
                    .inflate(R.layout.alert_dialog_no_internet, null)
                builder.setView(l_view)

                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                val btnRetry = l_view.findViewById<AppCompatButton>(R.id.btn_retry)
                btnRetry.setOnClickListener {
                    dialog.dismiss()
                    onReceive(context, intent)
                    main()
                }
            }
        }
    }

    private fun showTotalCase(query: String) {
        val client = AsyncHttpClient()
        val url = "https://disease.sh/v3/covid-19/historical/$query?lastdays=30"
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
                    val timeline = responseObject.getJSONObject("timeline")
                    val cases = timeline.getJSONObject("cases")
                    val recoveredCase = timeline.getJSONObject("recovered")
                    val deathCase = timeline.getJSONObject("deaths")

                    val listCases = ArrayList<Entry>()
                    val listActive = ArrayList<Entry>()
                    val listRecovered = ArrayList<Entry>()
                    val listDeath = ArrayList<Entry>()
                    var j = 1f

                    for (i in 8 downTo 2) {
                        var getCases = 0

                        if(cases.getInt(DateUtils.getDate(-i)) != 0)
                            getCases = cases.getInt(DateUtils.getDate(-i)) - cases.getInt(DateUtils.getDate(-(i + 1)))

                        listCases.add(Entry(j, getCases.toFloat()))

                        var getRecovered = 0
                        if(recoveredCase.getInt(DateUtils.getDate(-i)) != 0) {
                         getRecovered = recoveredCase.getInt(DateUtils.getDate(-i)) - recoveredCase.getInt(DateUtils.getDate(-(i + 1)))
                        }

                        listRecovered.add(Entry(j, getRecovered.toFloat()))
                        var getDeath = 0

                        if(deathCase.getInt(DateUtils.getDate(-i)) != 0)
                            getDeath = deathCase.getInt(DateUtils.getDate(-i)) - deathCase.getInt(DateUtils.getDate(-(i + 1))
                        )

                        listDeath.add(Entry(j, getDeath.toFloat()))

                        if(getCases == 0 || getRecovered == 0)
                            listActive.add(Entry(j, 0f))
                        else {
                            val getActive = getCases - (getRecovered - getDeath)
                            listActive.add(Entry(j, getActive.toFloat()))
                        }
                        Log.d("Data", getCases.toString())
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

                    binding.swiperefresh.isRefreshing = false

                } catch (e: java.lang.Exception) {
                    Toast.makeText(c, e.message.toString(), Toast.LENGTH_LONG).show()
                    binding.swiperefresh.isRefreshing = false
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Toast.makeText(c, error?.message.toString(), Toast.LENGTH_LONG).show()
                binding.swiperefresh.isRefreshing = false
            }

        })

    }

    fun setStatistic(kasus: ArrayList<Entry>, color: Int) {
        val kasusLineDataSet = LineDataSet(kasus, "Kasus")
        kasusLineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        kasusLineDataSet.color = resources.getColor(R.color.bgColorPrimary)
        kasusLineDataSet.setCircleColor(resources.getColor(color))
        kasusLineDataSet.setDrawValues(false)
        kasusLineDataSet.setDrawFilled(false)
        kasusLineDataSet.disableDashedLine()
        kasusLineDataSet.setColors(resources.getColor(color))

        binding.lineChart.setTouchEnabled(false)
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.setPinchZoom(false)
        binding.lineChart.setDrawGridBackground(false)
        binding.lineChart.setDrawBorders(false)

        binding.lineChart.xAxis.setDrawGridLines(false)
        binding.lineChart.axisLeft.setDrawAxisLine(false)

        binding.lineChart.xAxis.textColor = resources.getColor(R.color.colorPrimary)
        binding.lineChart.xAxis.textSize = 12f

        binding.lineChart.axisLeft.textColor = resources.getColor(R.color.colorPrimary)
        binding.lineChart.axisLeft.textSize = 12f

        //Setup Legend
        val legend = binding.lineChart.legend
        legend.isEnabled = false

        val date = ArrayList<String>();
        for (i in 9 downTo 2) {
            date.add(DateUtils.getDateStatistic(-i))
        }

        val tanggal = AxisDateFormatter(date.toArray(arrayOfNulls<String>(date.size)))
        binding.lineChart.xAxis?.valueFormatter = tanggal;

        binding.lineChart.description.isEnabled = false
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.xAxis.spaceMax = 0.5f

        binding.lineChart.data = LineData(kasusLineDataSet)
        binding.lineChart.animateY(100, Easing.Linear)
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

    private fun setBarChart(
        confirmed: BigDecimal,
        recovered: BigDecimal,
        death: BigDecimal,
        totalCase: BigDecimal,
        countryName: String
    ) {
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()

        listPie.add(PieEntry(confirmed.toFloat()))
        listColors.add(resources.getColor(R.color.yellow_primary))
        listPie.add(PieEntry(recovered.toFloat()))
        listColors.add(resources.getColor(R.color.green_custom))
        listPie.add(PieEntry(death.toFloat()))
        listColors.add(resources.getColor(R.color.red_custom))

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(0f)
        pieData.setValueTextColor(resources.getColor(android.R.color.transparent))
        binding.pieChart.data = pieData

        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        //set text center
        binding.pieChart.centerText = countryName.capitalize()
        binding.pieChart.setCenterTextColor(resources.getColor(R.color.colorPrimary))
        binding.pieChart.setCenterTextSize(22f)
        // val myFont = Typeface.createFromAsset(c?.assets, "font/montserrat_black.ttf")
        binding.pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)

        binding.pieChart.legend.isEnabled = false // hide tags labels

        binding.pieChart.setHoleColor(android.R.color.transparent)
        binding.pieChart.setTransparentCircleColor(android.R.color.white)

        binding.pieChart.holeRadius = 75f

        binding.pieChart.setDrawEntryLabels(false)
        binding.pieChart.description.isEnabled = false

        Log.d(TAG, listConfirm.size.toString())
    }

    fun blue() {
        setBlue()
        resetYellow()
        resetGreen()
        resetRed()
    }

    fun yellow() {
        setYellow()
        resetGreen()
        resetRed()
        resetBlue()
    }

    fun green() {
        setGreen()
        resetRed()
        resetYellow()
        resetBlue()
    }

    fun red() {
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

    override fun onStart() {
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        c.registerReceiver(broadcastReceiver, intent)
        super.onStart()
    }

    override fun onStop() {
        c.unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

}
