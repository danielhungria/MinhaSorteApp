package br.com.minhasortemegasena.ui.main

import androidx.lifecycle.ViewModel
import br.com.minhasortemegasena.adapter.MainAdapter
import br.com.minhasortemegasena.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    fun submitList(mainAdapter: MainAdapter, randomNumber: List<Int>){
        mainAdapter.submitList(randomNumber.map {
            it.toString()
        })
    }

    fun randomNumber(sliderValue: Int): List<Int> {
        return List(sliderValue + 5) { Random.nextInt(1..60) }
            .distinct()
            .shuffled()
            .take(sliderValue)
    }

}