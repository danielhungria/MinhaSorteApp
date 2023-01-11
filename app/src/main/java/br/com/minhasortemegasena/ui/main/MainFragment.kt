package br.com.minhasortemegasena.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.minhasortemegasena.databinding.FragmentMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random
import kotlin.random.nextInt

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var mAdView: AdView

    private val sliderNumber: Int = 25

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSlider()
        setupButton()
        mAdView = binding.adViewMainFragment
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        viewModel.getLotteryData()
    }

    private fun setupSlider() {
        binding.sliderFragmentMain.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                val sliderN = slider.value.toInt()
//                Log.i("Fragment", "onStopTrackingTouch: ${slider.value.toInt()}")
//                sliderNumber = slider.value.toInt()
//                Log.i("Fragment", "setupslider: $sliderNumber")

                if (sliderN >= 7) binding.cardViewNumber7FragmentMain.alpha = 1F else binding.cardViewNumber7FragmentMain.alpha = 0F
                if (sliderN >= 8) binding.cardViewNumber8FragmentMain.alpha = 1F else binding.cardViewNumber8FragmentMain.alpha = 0F
                if (sliderN >= 9) binding.cardViewNumber9FragmentMain.alpha = 1F else binding.cardViewNumber9FragmentMain.alpha = 0F
                if (sliderN >= 10) binding.cardViewNumber10FragmentMain.alpha = 1F else binding.cardViewNumber10FragmentMain.alpha = 0F
                if (sliderN >= 11) binding.cardViewNumber11FragmentMain.alpha = 1F else binding.cardViewNumber11FragmentMain.alpha = 0F
                if (sliderN >= 12) binding.cardViewNumber12FragmentMain.alpha = 1F else binding.cardViewNumber12FragmentMain.alpha = 0F
                if (sliderN >= 13) binding.cardViewNumber13FragmentMain.alpha = 1F else binding.cardViewNumber13FragmentMain.alpha = 0F
                if (sliderN >= 14) binding.cardViewNumber14FragmentMain.alpha = 1F else binding.cardViewNumber14FragmentMain.alpha = 0F
                if (sliderN == 15) binding.cardViewNumber15FragmentMain.alpha = 1F else binding.cardViewNumber15FragmentMain.alpha = 0F

                setupButton()
            }
        })
    }

    private fun setupButton() = with(binding) {
        buttonFragmentMain.setOnClickListener {
//            subtract
//            val number: List<Int> = listOf(7,4,12,34,56,43)
//            Log.i("Fragment", "setupButton: $sliderNumber")
            val random = List(sliderNumber+3){ Random.nextInt(1..60)}
//                .subtract(number)
                .distinct()
                .shuffled()

            textViewNumber1FragmentMain.text = random[0].toString()
            textViewNumber2FragmentMain.text = random[1].toString()
            textViewNumber3FragmentMain.text = random[2].toString()
            textViewNumber4FragmentMain.text = random[3].toString()
            textViewNumber5FragmentMain.text = random[4].toString()
            textViewNumber6FragmentMain.text = random[5].toString()
            textViewNumber7FragmentMain.text = random[6].toString()
            textViewNumber8FragmentMain.text = random[7].toString()
            textViewNumber9FragmentMain.text = random[8].toString()
            textViewNumber10FragmentMain.text = random[9].toString()
            textViewNumber11FragmentMain.text = random[10].toString()
            textViewNumber12FragmentMain.text = random[11].toString()
            textViewNumber13FragmentMain.text = random[12].toString()
            textViewNumber14FragmentMain.text = random[13].toString()
            textViewNumber15FragmentMain.text = random[14].toString()

            Log.i("Fragment", "setupButton: $random")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

}