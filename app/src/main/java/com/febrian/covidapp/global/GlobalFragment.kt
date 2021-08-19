package com.febrian.covidapp.global

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.febrian.covidapp.MapActivity
import com.febrian.covidapp.R
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.databinding.FragmentGlobalBinding
import com.febrian.covidapp.news.utils.InternetConnection
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.analytics.type.ReportPolicy
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.SupportMapFragment
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MapStyleOptions
import com.huawei.hms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GlobalFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: HuaweiMap
    private lateinit var binding: FragmentGlobalBinding
    private lateinit var darkStyle: MapStyleOptions
    
    private lateinit var c : Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        
        c = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGlobalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HiAnalyticsTools.enableLog()
        val instance: HiAnalyticsInstance = HiAnalytics.getInstance(c)
        instance.setAnalyticsEnabled(true)
        instance.setUserProfile("userKey", "value")
        instance.setAutoCollectionEnabled(true)
        instance.regHmsSvcEvent()
        val launch: ReportPolicy = ReportPolicy.ON_APP_LAUNCH_POLICY
        val report: MutableSet<ReportPolicy> = HashSet<ReportPolicy>()

        report.add(launch)

        instance.setReportPolicies(report)

        val bundle = Bundle()
        bundle.putString("Global", "Global")
        instance.onEvent("Global", bundle)

        main()

        binding.refreshLayout.setOnRefreshListener {
            main()
        }
    }

    private fun main() {

        darkStyle = MapStyleOptions.loadRawResourceStyle(c, R.raw.mapstyle_night)

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        binding.tgl.text = currentDate

        ApiService.newService.getGlobalDataV2().enqueue(object : Callback<GlobalResponse>{
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        setGlobalData(body)
                    }

                    binding.refreshLayout.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                binding.refreshLayout.isRefreshing = false
                Toast.makeText(c, t.message, Toast.LENGTH_LONG).show()
            }

        })

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapHome) as SupportMapFragment

        binding.btnMaps.setOnClickListener {
            startActivity(Intent(c, MapActivity::class.java))
        }

        mapFragment.getMapAsync(this)
    }

    private fun isDarkModeOn(): Boolean {

        return when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    override fun onMapReady(googleMap: HuaweiMap) {
        mMap = googleMap

        if (isDarkModeOn()) googleMap.setMapStyle(darkStyle) else googleMap.setMapStyle(null)

        mMap.uiSettings.isZoomControlsEnabled = false

        var location = c.resources?.configuration?.locale?.displayCountry

        if(location == "" || location == null)
            location = "Indonesia"

        binding.country.text = location
        var addressList : List<Address>? = null
        var latLng : LatLng? = null
        val geocoder = Geocoder(c)
        var address : Address? = null
        try {
            addressList = geocoder.getFromLocationName(location, 1)
            address = addressList!![0]
            latLng = LatLng(address!!.latitude, address.longitude)
            mMap.addMarker(MarkerOptions().position(latLng).title(location.toString()))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address.latitude, address.longitude), 3f))
        } catch (e: IOException) {
            Toast.makeText(c, e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setGlobalData(body: GlobalResponse) {
        val totalCase = body.cases?.toBigDecimal()!!
        val recovered = body.recovered?.toBigDecimal()!!
        val deaths = body.deaths?.toBigDecimal()!!
        val confirmed = totalCase - (recovered + deaths)

        binding.confirmed.text = NumberFormat.getInstance().format(confirmed).toString()
        binding.recovered.text = NumberFormat.getInstance().format(recovered).toString()
        binding.deceased.text = NumberFormat.getInstance().format(deaths).toString()
        setBarChart(confirmed, recovered, deaths, totalCase)
    }

    @SuppressLint("ResourceType")
    private fun setBarChart(
        confirmed: BigDecimal,
        recovered: BigDecimal,
        death: BigDecimal,
        totalCase: BigDecimal
    ) {
        val listPie = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()

        listPie.add(PieEntry(confirmed.toFloat()))
        c?.resources?.getColor(R.color.yellow_primary)?.let { listColors.add(it) }
        listPie.add(PieEntry(recovered.toFloat()))
        c?.resources?.getColor(R.color.green_custom)?.let { listColors.add(it) }
        listPie.add(PieEntry(death.toFloat()))
        c?.resources?.getColor(R.color.red_custom)?.let { listColors.add(it) }

        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(0f)
        c?.resources?.getColor(android.R.color.transparent)?.let {
            pieData.setValueTextColor(
                it
            )
        }
        binding.pieChart.data = pieData

        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        //set text center
        binding.pieChart.centerText = "Total Case\n${NumberFormat.getInstance().format(totalCase)}"
        binding.pieChart.setCenterTextColor(Color.argb(255, 80,125,188))
        binding.pieChart.setCenterTextSize(18f)
        binding.pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)

        binding.pieChart.legend.isEnabled = false // hide tags labels

        binding.pieChart.setHoleColor(android.R.color.transparent)
        binding.pieChart.setTransparentCircleColor(android.R.color.white)

        binding.pieChart.holeRadius = 75f

        binding.pieChart.setDrawEntryLabels(false)
        binding.pieChart.description.isEnabled = false
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!InternetConnection.isConnected(context)) {

                val builder = AlertDialog.Builder(c)
                val l_view = LayoutInflater.from(c).inflate(R.layout.alert_dialog_no_internet,null)
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

    override fun onStart() {
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        c?.registerReceiver(broadcastReceiver, intent)
        super.onStart()
    }

    override fun onStop() {
        c?.unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

}