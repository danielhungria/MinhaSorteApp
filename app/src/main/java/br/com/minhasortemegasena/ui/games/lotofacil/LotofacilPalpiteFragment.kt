package br.com.minhasortemegasena.ui.games.lotofacil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import br.com.minhasortemegasena.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSlider()
        setupAD()
        setupRecycler()
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.status_bar_lotofacil)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LotofacilPalpiteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.status_bar_default)
    }

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
                    viewModel.generateRandomNumbers(sliderValue)
                    viewModel.submitList(mainAdapter)
                }
            }
            layoutManager = GridLayoutManager(requireContext(), 6).apply {

            }
        }
    }



    private fun setupAD() {
        mAdView = binding.adViewFragmentPalpiteLotofacil
        mAdView2 = binding.adView2Fragment
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView2.loadAd(adRequest)
    }


}
