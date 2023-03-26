package br.com.minhasortemegasena.ui.games.federal

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
class FederalPalpiteViewModel @Inject constructor(): ViewModel() {

    private val _randomNumberList = MutableLiveData<List<Int>>()
     val randomNumberList: LiveData<List<Int>> = _randomNumberList

    var adLoad: Int = 0

    fun generateRandomNumbers() {
        _randomNumberList.value = randomNumber()
    }

    private fun randomNumber(): List<Int> {
        val list = mutableListOf<Int>()
            val randomNumber = (1..9999).random()
            if (randomNumber !in list) {
                list.add(randomNumber)
            }
        return list
    }

}