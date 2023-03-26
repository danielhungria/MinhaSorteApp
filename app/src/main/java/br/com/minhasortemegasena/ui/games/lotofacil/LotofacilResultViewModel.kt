package br.com.minhasortemegasena.ui.games.lotofacil

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.minhasortemegasena.model.LotteryModel
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LotofacilResultViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val listLotteryModel = MutableLiveData<LotteryModel>()
    val errorMessage = MutableLiveData<String>()
    var contestNumberViewModel = ""
    var adLoad: Int = 0


    fun getLotteryData() {
        val request = mainRepository.getLotteryData("lotofacil")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                listLotteryModel.postValue(response.body())
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getLotteryWithContestNumber() {
        val request = mainRepository.getLotteryWithContestNumber(contestNumberViewModel,"lotofacil")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                listLotteryModel.postValue(response.body())
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

}