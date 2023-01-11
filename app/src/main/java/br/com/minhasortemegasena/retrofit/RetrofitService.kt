package br.com.minhasortemegasena.retrofit

import br.com.minhasortemegasena.model.LotteryModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://servicebus2.caixa.gov.br/"
const val contest_number = "2550"
const val lottery_name = "megasena"

interface RetrofitService {

    @GET("portaldeloterias/api/$lottery_name/$contest_number")
    fun getLotteryData(): Call<LotteryModel>

    companion object {

        private val retrofitService: RetrofitService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(RetrofitService::class.java)
        }

        fun getInstance(): RetrofitService{
            return retrofitService
        }

    }

}