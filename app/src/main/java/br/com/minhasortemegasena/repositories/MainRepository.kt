package br.com.minhasortemegasena.repositories

import br.com.minhasortemegasena.retrofit.RetrofitService
import javax.inject.Inject

class MainRepository @Inject constructor(private val retrofitService: RetrofitService) {

    fun getLotteryData(contestNumber: String) = retrofitService.getLotteryData(contestNumber)

}