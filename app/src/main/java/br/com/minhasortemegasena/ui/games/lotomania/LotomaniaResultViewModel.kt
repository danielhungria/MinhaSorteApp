package br.com.minhasortemegasena.ui.games.lotomania

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.minhasortemegasena.model.LotteryModel
import br.com.minhasortemegasena.model.ResultFederalModel
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class LotomaniaResultViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val listLotteryModel = MutableLiveData<LotteryModel>()
    val errorMessage = MutableLiveData<String>()
    var contestNumberViewModel = ""
    var lastContestNumberViewModel = 0
    var adLoad: Int = 0

    private val _listaRateioPremio = MutableLiveData<List<ResultFederalModel>>()
    val listaRateioPremio: LiveData<List<ResultFederalModel>>
        get() = _listaRateioPremio

    private val _listLastLotteryModel = MutableLiveData<LotteryModel>()
    val listLastLotteryModel: LiveData<LotteryModel>
        get() = _listLastLotteryModel


    fun getLotteryData() {
        val request = mainRepository.getLotteryData("lotomania")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                listLotteryModel.postValue(response.body())
                _listaRateioPremio.postValue(response.body()?.listaRateioPremio)
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getLastLotteryData() {
        val request = mainRepository.getLotteryData("lotomania")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                _listLastLotteryModel.postValue(response.body())
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getLotteryWithContestNumber() {
        val request = mainRepository.getLotteryWithContestNumber(contestNumberViewModel,"lotomania")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                listLotteryModel.postValue(response.body())
                _listaRateioPremio.postValue(response.body()?.listaRateioPremio)
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

}