package br.com.minhasortemegasena.ui.games.lotofacil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import br.com.minhasortemegasena.adapter.MainAdapter
import br.com.minhasortemegasena.databinding.LotofacilPalpiteFragmentBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LotofacilPalpiteFragment : Fragment() {

    private lateinit var binding: LotofacilPalpiteFragmentBinding

    private lateinit var mAdView: AdView
    private lateinit var mAdView2: AdView

    private val viewModel: LotofacilPalpiteViewModel by viewModels()

    private val mainAdapter = MainAdapter()

    private var sliderValue = 15

    private fun setupSlider() {

        binding.sliderFragmentPalpiteLotofacil.addOnChangeListener { slider, value, fromUser ->
            val sliderN = value.toInt()
            sliderValue = sliderN
        }

    }

    private fun setupRecycler() {
        binding.gridlayoutFragmentPalpiteLotofacil.apply {
            adapter = mainAdapter.apply {
                binding.buttonFragmentPalpiteLotofacil.setOnClickListener {
                    val randomNumber = viewModel.randomNumber(sliderValue)
                    viewModel.submitList(mainAdapter, randomNumber)
                }
            }
            layoutManager = GridLayoutManager(requireContext(), 6).apply {

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSlider()
        setupAD()
        setupRecycler()

    }

    private fun setupAD() {
        mAdView = binding.adViewFragmentPalpiteLotofacil
        mAdView2 = binding.adView2Fragment
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView2.loadAd(adRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LotofacilPalpiteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
