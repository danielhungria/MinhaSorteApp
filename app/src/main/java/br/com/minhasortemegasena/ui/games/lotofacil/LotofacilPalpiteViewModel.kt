package br.com.minhasortemegasena.ui.games.lotofacil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.minhasortemegasena.adapter.MainAdapter
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@HiltViewModel
class LotofacilPalpiteViewModel @Inject constructor(): ViewModel() {

    private val _randomNumberList = MutableLiveData<List<Int>>()
    private val randomNumberList: LiveData<List<Int>> = _randomNumberList

    fun generateRandomNumbers(sliderValue: Int) {
        _randomNumberList.value = randomNumber(sliderValue)
    }

    private fun randomNumber(sliderValue: Int): List<Int> {
        val list = mutableListOf<Int>()
        while (list.size < sliderValue) {
            val randomNumber = (1..25).random()
            if (randomNumber !in list) {
                list.add(randomNumber)
            }
        }
        return list
    }

    fun submitList(mainAdapter: MainAdapter){
        mainAdapter.submitList(randomNumberList.value?.map {
            it.toString()
        })
    }
}