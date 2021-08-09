package com.febrian.covidapp.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import com.febrian.covidapp.PushKitService
import com.febrian.covidapp.R
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.FragmentHomeBinding
import com.febrian.covidapp.global.DateUtils
import com.febrian.covidapp.global.GlobalResponse
import com.febrian.covidapp.home.response.CountryResponse
import com.febrian.covidapp.news.utils.InternetConnection
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.HAEventType
import com.huawei.hms.analytics.type.HAParamType
import com.huawei.hms.analytics.type.ReportPolicy
import com.huawei.hms.common.ApiException
import com.huawei.hms.framework.common.ContextCompat.startService
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.DelicateCoroutinesApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.sql.Timestamp
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
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "covid channel"
        const val TAG = "Home Activity"
    }



    val listConfirm: MutableList<Int> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendNotification()
        getToken()

        HiAnalyticsTools.enableLog()
        val instance : HiAnalyticsInstance = HiAnalytics.getInstance(view.context)
        instance.setAnalyticsEnabled(true)
        instance.setUserProfile("userKey", "value")
        instance.setAutoCollectionEnabled(true)
        instance.regHmsSvcEvent()
        val launch : ReportPolicy = ReportPolicy.ON_APP_LAUNCH_POLICY
        val report : MutableSet<ReportPolicy> = HashSet<ReportPolicy>()

        report.add(launch)

        instance.setReportPolicies(report)

        val bundle = Bundle()
        bundle.putString("exam_difficulty", "high")
        bundle.putString("exam_level", "1-1")
        bundle.putString("exam_time", "20190520-08")
        instance.onEvent("begin_examination", bundle)
// Enable tracking of the predefined event in proper positions of the code.
        val bundle_pre = Bundle()
        bundle_pre.putString(HAParamType.PRODUCTID, "item_ID")
        bundle_pre.putString(HAParamType.PRODUCTNAME, "name")
        bundle_pre.putString(HAParamType.CATEGORY, "category")
        bundle_pre.putLong(HAParamType.QUANTITY, 100L)
        bundle_pre.putDouble(HAParamType.PRICE, 10.01)
        bundle_pre.putDouble(HAParamType.REVENUE, 10.0)
        bundle_pre.putString(HAParamType.CURRNAME, "currency")
        bundle_pre.putString(HAParamType.PLACEID, "location_ID")
        instance.onEvent(HAEventType.ADDPRODUCT2WISHLIST, bundle_pre)

        main()
        binding.swiperefresh.setOnRefreshListener {
            binding.searchView.setQuery("", false)
            main()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun setLocale(s: String, lang : String, i : Int) {
        val locale: Locale = Locale(s)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.locale = locale
        view?.context?.resources?.updateConfiguration(
            configuration,
            context?.resources?.displayMetrics
        )
    }

    internal fun main() {

        val sharedPref = view?.context?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lang = sharedPref?.getString("Language", "en")
        val index = sharedPref?.getInt("Index", 0)
        val value = sharedPref?.getString("Value", "English")
        setLocale(lang.toString(), value.toString(), index!!)

        val location = view?.context?.resources?.configuration?.locale?.displayCountry

        showStatistic(location.toString())
        showTotalCase(location.toString())

        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        binding.tgl.text = currentDate
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showStatistic(query)
                showTotalCase(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                showStatistic(newText)
//                showTotalCase(newText)
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
                                binding.totalCaseValue.text = NumberFormat.getInstance().format(totalCase).toString()
                                setBarChart(confirmed, recovered, deaths, totalCase, query)
                            }
                            binding.swiperefresh.isRefreshing = false
                        } catch (e: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {

                }

            })

    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!InternetConnection.isConnected(context)) {
                
                val builder = AlertDialog.Builder(view?.context)
                val l_view = LayoutInflater.from(view?.context).inflate(R.layout.alert_dialog_no_internet,null)
                builder.setView(l_view)

                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                val btnRetry = l_view.findViewById<AppCompatButton>(R.id.btn_retry)
                btnRetry.setOnClickListener{
                    dialog.dismiss()
                    onReceive(context,intent)
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
                        val getCases =
                            cases.getInt(DateUtils.getDate(-i)) - cases.getInt(DateUtils.getDate(-(i + 1)))
                            listCases.add(Entry(j, getCases.toFloat()))
                        val getRecovered =
                            recoveredCase.getInt(DateUtils.getDate(-i)) - recoveredCase.getInt(
                                DateUtils.getDate(-(i + 1))
                            )
                            listRecovered.add(Entry(j, getRecovered.toFloat()))
                        val getDeath = deathCase.getInt(DateUtils.getDate(-i)) - deathCase.getInt(
                            DateUtils.getDate(-(i + 1))
                        )
                        listDeath.add(Entry(j, getDeath.toFloat()))
                        val getActive = getCases - (getRecovered - getDeath)
                            listActive.add(Entry(j, getActive.toFloat()))

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
                    Log.d("P", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("P", error?.message.toString())
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
        countryName : String
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
        // val myFont = Typeface.createFromAsset(view?.context?.assets, "font/montserrat_black.ttf")
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
        view?.context?.registerReceiver(broadcastReceiver, intent)
        super.onStart()
    }

    override fun onStop() {
        view?.context?.unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(){
        val jakartaZone = ZoneId.of("Asia/Jakarta")
        val asiaJakartaCurrentDate = ZonedDateTime.now(jakartaZone)

        val asiaJakarta = asiaJakartaCurrentDate.format(DateTimeFormatter.ofPattern("HH:mm"))

        if(asiaJakarta == "13:22") {
            Log.d("TEST", asiaJakarta.toString())
            val mNotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val mBuilder = view?.context?.let {
                NotificationCompat.Builder(it, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_bookmark_24)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_notifications_24))
                    .setContentTitle("Data Updated!")
                    .setContentText("Data Updated! 2")
                    .setSubText("Data Updated! 3")
                    .setAutoCancel(true)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                /* Create or update. */
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = CHANNEL_NAME
                mBuilder?.setChannelId(CHANNEL_ID)
                mNotificationManager.createNotificationChannel(channel)
            }

            val notification = mBuilder?.build()
            mNotificationManager.notify(NOTIFICATION_ID, notification)
        }

    }

    private fun getToken() {
        // Create a thread.
        object : Thread() {
            override fun run() {
                try {
                    // Obtain the app ID from the agconnect-service.json file.
                    val appId = "104531441"

                    // Set tokenScope to HCM.
                    val tokenScope = "HCM"
                    val token = HmsInstanceId.getInstance(view?.context).getToken(appId, tokenScope)
                    Log.i(TAG, "get token:$token")

                    // Check whether the token is empty.
                    if (!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token)
                    }
                } catch (e: ApiException) {
                    Log.e(TAG, "get token failed, ${e.message}")
                }
            }
        }.start()
    }
    private fun sendRegTokenToServer(token: String?) {
        Log.i(TAG, "sending token to server. token:$token")
    }

    override fun onResume() {
        super.onResume()
        val mStartServiceIntent = Intent(view?.context, PushKitService::class.java)
        view?.context?.startService(mStartServiceIntent)
    }
}