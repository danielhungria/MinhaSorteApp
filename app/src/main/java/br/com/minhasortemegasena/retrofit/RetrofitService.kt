package br.com.minhasortemegasena.retrofit

import br.com.minhasortemegasena.model.LotteryModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://servicebus2.caixa.gov.br/"

interface RetrofitService {

    @GET("portaldeloterias/api/{lottery_name}/")
    fun getLotteryData(
        @Path("lottery_name") lotteryName: String
    ): Call<LotteryModel>

    @GET("portaldeloterias/api/{lottery_name}/{contest_number}")
    fun getLotteryWithContestNumber(
        @Path("contest_number") contestNumber: String,
        @Path("lottery_name") lotteryName: String
    ): Call<LotteryModel>

}