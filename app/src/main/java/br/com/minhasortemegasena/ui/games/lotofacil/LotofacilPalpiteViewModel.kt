package br.com.minhasortemegasena.ui.games.lotofacil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.minhasortemegasena.adapter.MainAdapter
import br.com.minhasortemegasena.model.PalpiteModel
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@HiltViewModel
class LotofacilPalpiteViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

    private val _randomNumberList = MutableLiveData<List<Int>>()
    private val randomNumberList: LiveData<List<Int>> = _randomNumberList
    var adLoad: Int = 0


    fun generateRandomNumbers(sliderValue: Int) {
        _randomNumberList.value = randomNumber(sliderValue)
    }

    private fun randomNumber(sliderValue: Int): List<Int> {
        val list = mutableListOf<Int>()
        val random = Random(System.currentTimeMillis())
        while (list.size < sliderValue) {
            val randomNumber = random.nextInt(25) + 1
            if (randomNumber !in list) {
                list.add(randomNumber)
            }
        }
        return list.sorted()
    }

    fun submitList(mainAdapter: MainAdapter){
        mainAdapter.submitList(randomNumberList.value?.map {
            it.toString()
        })
    }

    fun onSaveEvent(){
        viewModelScope.launch {
            val list = _randomNumberList.value?.map { it.toString().toInt() }
            val palpiteToSave = list?.let {
                PalpiteModel(
                    palpiteNumbers = it,
                    typeGame = "lotofacil"
                )
            }
            palpiteToSave?.let { mainRepository.insert(it) }
        }
    }
}