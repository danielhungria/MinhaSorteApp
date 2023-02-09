package br.com.minhasortemegasena.retrofit

import br.com.minhasortemegasena.model.LotteryModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://servicebus2.caixa.gov.br/"
const val lottery_name = "megasena"

interface RetrofitService {

    @GET("portaldeloterias/api/$lottery_name/{NUMBER}")
    fun getLotteryData(
        @Path("NUMBER") contestNumber: String
    ): Call<LotteryModel>

}