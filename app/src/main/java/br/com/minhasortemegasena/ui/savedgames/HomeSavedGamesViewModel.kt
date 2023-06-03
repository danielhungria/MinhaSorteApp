package br.com.minhasortemegasena.ui.savedgames

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.minhasortemegasena.model.LotteryModel
import br.com.minhasortemegasena.model.PalpiteModel
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class HomeSavedGamesViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    var adLoad: Int = 0

    private val _palpiteModel = MutableLiveData<List<PalpiteModel>>()
    val palpiteModel: LiveData<List<PalpiteModel>>
        get() = _palpiteModel

    private val _listLotteryModel = MutableLiveData<LotteryModel>()
    val listLotteryModel: LiveData<LotteryModel>
        get() = _listLotteryModel

    private val _listLotteryModelLotofacil = MutableLiveData<LotteryModel>()
    val listLotteryModelLotofacil: LiveData<LotteryModel>
        get() = _listLotteryModelLotofacil

    private val _listLotteryModelLotomania = MutableLiveData<LotteryModel>()
    val listLotteryModelLotomania: LiveData<LotteryModel>
        get() = _listLotteryModelLotomania

    private val _listLotteryModelQuina = MutableLiveData<LotteryModel>()
    val listLotteryModelQuina: LiveData<LotteryModel>
        get() = _listLotteryModelQuina

    private val _listLotteryModelFederal = MutableLiveData<LotteryModel>()
    val listLotteryModelFederal: LiveData<LotteryModel>
        get() = _listLotteryModelFederal

    val errorMessage = MutableLiveData<String>()



    fun fetchItemList() {
        viewModelScope.launch {
            mainRepository.getAllPalpites().collect {
                _palpiteModel.postValue(it)
            }
        }
    }

    fun checkList(): Boolean {
        return _palpiteModel.value.isNullOrEmpty()
    }


    fun getLotteryDataMegasena() {
        val request = mainRepository.getLotteryData("megasena")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                _listLotteryModel.postValue(response.body())
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getLotteryDataLotofacil() {
        val request = mainRepository.getLotteryData("lotofacil")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                _listLotteryModelLotofacil.postValue(response.body())
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getLotteryDataLotomania() {
        val request = mainRepository.getLotteryData("lotomania")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                _listLotteryModelLotomania.postValue(response.body())
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getLotteryDataFederal() {
        val request = mainRepository.getLotteryData("federal")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                _listLotteryModelFederal.postValue(response.body())
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getLotteryDataQuina() {
        val request = mainRepository.getLotteryData("quina")
        request.enqueue(object : Callback<LotteryModel> {
            override fun onResponse(call: Call<LotteryModel>, response: Response<LotteryModel>) {
                _listLotteryModelQuina.postValue(response.body())
            }
            override fun onFailure(call: Call<LotteryModel>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun deleteItem(palpiteModel: PalpiteModel) {
        viewModelScope.launch {
            mainRepository.delete(palpiteModel)
        }
    }

}