package com.febrian.covidapp.global

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.febrian.covidapp.R
import com.febrian.covidapp.api.ApiService
import com.febrian.covidapp.home.response.CountryResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await
import retrofit2.awaitResponse

class GlobalActivity : AppCompatActivity() {
    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global)

        GlobalScope.launch(Dispatchers.IO) {
            val response = ApiService.globalDataCovid.getGlobalData().awaitResponse()

            if(response.isSuccessful){
                Log.d("TAG", response.body()?.confirmed.toString())

                val body = response.body()
                if (body != null) {
                    setGlobalData(body)
                }
            }
        }
    }

    private fun setGlobalData(body : CountryResponse) {
        val confirmed =  body.confirmed?.value!!.toInt()
        val recovered =  body.recovered?.value!!.toInt()
        val deaths =  body.deaths?.value!!.toInt()
        val active = confirmed - (recovered + deaths)

    }
}