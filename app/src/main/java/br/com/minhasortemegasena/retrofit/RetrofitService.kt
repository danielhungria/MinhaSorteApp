package br.com.minhasortemegasena.retrofit

import br.com.minhasortemegasena.model.LotteryModel
import retrofit2.Call
import retrofit2.http.GET

const val BASE_URL = "https://servicebus2.caixa.gov.br/"
const val contest_number = "2554"
const val lottery_name = "megasena"

interface RetrofitService {

    @GET("portaldeloterias/api/$lottery_name/$contest_number")
    fun getLotteryData(): Call<LotteryModel>

}