package br.com.minhasortemegasena.ui.games.megasena

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.adapter.MainAdapter
import br.com.minhasortemegasena.databinding.FragmentMainBinding
import br.com.minhasortemegasena.util.Constants.AD_COUNT
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var mAdView: AdView
    private lateinit var mAdView2: AdView
    private val viewModel: MainViewModel by viewModels()
    private val mainAdapter = MainAdapter()
    private var sliderValue = 6
    private var mInterstitialAd: InterstitialAd? = null


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
        setupAdInterstitial()
        setupButtonGenerateNumbers()
        setupButtonSavePalpite()
        AD_COUNT++
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupButtonSavePalpite() {
        binding.buttonSaveSortedNumber.setOnClickListener {
            setupAdInterstitial()
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirmar ação")
            builder.setMessage("Para salvar o palpite é necessário exibir um anúncio, deseja continuar?")
            builder.setPositiveButton("Sim") { _, _ ->
                Toast.makeText(requireContext(), "Palpite salvo com sucesso!", Toast.LENGTH_SHORT).show()
                binding.buttonSaveSortedNumber.postDelayed({
                    AD_COUNT = 2
                    setupAdInterstitial()
                    viewModel.onSaveEvent()
                }, 2000)
            }
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
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
            adapter = mainAdapter
            viewModel.submitList(mainAdapter)
            layoutManager = GridLayoutManager(requireContext(), 6)
        }
    }

    private fun setupButtonGenerateNumbers() {
        binding.buttonFragmentMain.setOnClickListener {
            viewModel.generateRandomNumbers(sliderValue)
            viewModel.submitList(mainAdapter)
            binding.scrollViewFragment.post {
                binding.scrollViewFragment.fullScroll(ScrollView.FOCUS_DOWN)
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

    private fun setupAdInterstitial() {
        if (AD_COUNT>=2){
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                requireContext(),
                getString(R.string.ad_view_interstitial_default),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        adError.toString().let { Log.d("Fragment", it) }
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d("Fragment", "Ad was loaded.")
                        mInterstitialAd = interstitialAd
                        mInterstitialAd?.show(requireActivity())
                        AD_COUNT=0
                    }
                }
            )
        }
    }
}