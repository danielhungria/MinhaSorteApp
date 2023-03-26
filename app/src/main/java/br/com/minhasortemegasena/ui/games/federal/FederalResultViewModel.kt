package br.com.minhasortemegasena.ui.games.federal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.minhasortemegasena.model.ListaRateioPremio
import br.com.minhasortemegasena.model.LotteryModel
import br.com.minhasortemegasena.model.ResultFederalModel
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class FederalResultViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _listLotteryModel = MutableLiveData<LotteryModel>()
    val listLotteryModel: LiveData<LotteryModel>
        get() = _listLotteryModel

    private val _listaRateioPremio = MutableLiveData<List<ResultFederalModel>>()
    val listaRateioPremio: LiveData<List<ResultFederalModel>>
        get() = _listaRateioPremio


    val errorMessage = MutableLiveData<String>()
    var contestNumberViewModel = ""

    var adLoad: Int = 0

    fun getLotteryData() {
        val request = mainRepository.getLotteryData("federal")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                _listLotteryModel.postValue(response.body())
                _listaRateioPremio.postValue(response.body()?.listaRateioPremio)
                response.body()?.listaRateioPremio?.let { rateios ->
                    val dezenas = response.body()?.listaDezenas
                    rateios.forEach { rateio ->
                        rateio.listaDezenas = dezenas ?: emptyList()
                    }
                    _listaRateioPremio.postValue(rateios)
                }
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }


    fun getLotteryWithContestNumber() {
        val request = mainRepository.getLotteryWithContestNumber(contestNumberViewModel, "federal")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                _listLotteryModel.postValue(response.body())
                _listaRateioPremio.postValue(response.body()?.listaRateioPremio)
                response.body()?.listaRateioPremio?.let { rateios ->
                    val dezenas = response.body()?.listaDezenas
                    rateios.forEach { rateio ->
                        rateio.listaDezenas = dezenas ?: emptyList()
                    }
                    _listaRateioPremio.postValue(rateios)
                }
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

}