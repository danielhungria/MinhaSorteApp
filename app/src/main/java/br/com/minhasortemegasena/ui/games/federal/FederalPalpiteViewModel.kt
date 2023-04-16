package br.com.minhasortemegasena.ui.games.federal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.minhasortemegasena.model.PalpiteModel
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class FederalPalpiteViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

    private val _randomNumberList = MutableLiveData<List<Int>>()
     val randomNumberList: LiveData<List<Int>> = _randomNumberList


    var adLoad: Int = 0

    fun generateRandomNumbers() {
        _randomNumberList.value = randomNumber()
    }

    private fun randomNumber(): List<Int> {
        val list = mutableListOf<Int>()
            val random = Random(System.currentTimeMillis())
            val randomNumber = random.nextInt(99999) + 1
//            val randomNumber = (1..9999).random()
            if (randomNumber !in list) {
                list.add(randomNumber)
            }
        return list
    }

    fun onSaveEvent(){
        viewModelScope.launch {
            val list = _randomNumberList.value?.map { it.toString().toInt() }
            val palpiteToSave = list?.let {
                PalpiteModel(
                    palpiteNumbers = it,
                    typeGame = "federal"
                )
            }
            palpiteToSave?.let { mainRepository.insert(it) }
        }
    }

}