package com.akso.realwear.app.app.callAPI

import android.util.Log
import com.akso.realwear.app.app.timeLog.TimeLogData
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object ServiceBuilder {

    val baseUrl = "https://devapimgmnt-dev-test.cfapps.eu20.hana.ondemand.com/Test/"

    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>) : T {
        return retrofit.create(service)
    }
}

class RestApiService {

    fun addTimelog(timelog: TimeLogData, onResult: (TimeLogData?) -> Unit){
    val response = ServiceBuilder.buildService(ApiInterface::class.java)
        response.ISendReq(RequestModel("", "")).enqueue(
            object: Callback<TimeLogData> {
                override fun onResponse(
                    call: Call<TimeLogData>,
                    response: Response<TimeLogData>

                ) {
                    Log.i("apiresponse", response.toString())
                }

                override fun onFailure(call: Call<TimeLogData>, t: Throwable) {
                    Log.i("apiresponse", t.toString())
                }
            }
        )
    }

    fun uploadImage() {

    }

}