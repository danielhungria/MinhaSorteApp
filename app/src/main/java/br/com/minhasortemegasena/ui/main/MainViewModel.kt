package br.com.minhasortemegasena.ui.main

import androidx.lifecycle.ViewModel
import br.com.minhasortemegasena.adapter.MainAdapter
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt


@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    var randomNumberList: List<Int> = emptyList()

    fun submitList(mainAdapter: MainAdapter){
        mainAdapter.submitList(randomNumberList.map {
            it.toString()
        })
    }

    fun randomNumber(sliderValue: Int): List<Int> {
        randomNumberList = List(sliderValue + 10) { Random.nextInt(1..60) }
            .distinct()
            .shuffled()
            .take(sliderValue)
        return randomNumberList
    }

}