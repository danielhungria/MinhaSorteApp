package br.com.minhasortemegasena.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.adapter.MainAdapter
import br.com.minhasortemegasena.databinding.FragmentMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var mAdView: AdView
    private lateinit var mAdView2: AdView

    private val viewModel: MainViewModel by viewModels()

    private val mainAdapter = MainAdapter()

    private var sliderValue = 6


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSlider()
        setupAD()
        setupRecycler()
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSlider() {

        binding.sliderFragmentMain.addOnChangeListener { slider, value, fromUser ->
            val sliderN = value.toInt()
            sliderValue = sliderN
        }

    }

    private fun setupRecycler() {
        binding.gridlayoutFragmentMain.apply {
            adapter = mainAdapter.apply {
                viewModel.submitList(mainAdapter)
                binding.buttonFragmentMain.setOnClickListener {
                    viewModel.randomNumber(sliderValue)
                    viewModel.submitList(mainAdapter)
                }
            }
            layoutManager = GridLayoutManager(requireContext(), 6).apply {

            }
        }
    }


    private fun setupAD() {
        mAdView = binding.adViewMainFragment
        mAdView2 = binding.adView2MainFragment
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView2.loadAd(adRequest)
    }



}