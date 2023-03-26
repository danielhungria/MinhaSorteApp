package br.com.minhasortemegasena.ui.games.federal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.databinding.FederalPalpiteFragmentBinding
import br.com.minhasortemegasena.util.Constants.AD_COUNT
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FederalPalpiteFragment : Fragment() {

    private lateinit var binding: FederalPalpiteFragmentBinding
    private lateinit var mAdView: AdView
    private lateinit var mAdView2: AdView
    private var mInterstitialAd: InterstitialAd? = null

    private val viewModel: FederalPalpiteViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAD()
        setupNumberGenerated()
        AD_COUNT++
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.status_bar_federal)
        val generatedNumber = viewModel.randomNumberList.value?.first()
        if (generatedNumber != null) {
            binding.textViewFragmentPalpiteFederal.text = generatedNumber.toString()
        }
        binding.toolbarLotofacilPalpite.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        setupAdInterstitial()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FederalPalpiteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.status_bar_default)
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.status_bar_federal)
    }

    private fun setupNumberGenerated() {
        binding.buttonFragmentPalpiteLotofacil.setOnClickListener {
            viewModel.generateRandomNumbers()
            val generatedNumber = viewModel.randomNumberList.value?.first().toString()
            val formattedNumber = String.format("%04d", generatedNumber.toInt())
            binding.textViewFragmentPalpiteFederal.text = formattedNumber
        }
    }

    private fun setupAD() {
        mAdView = binding.adViewFragmentPalpiteLotofacil
        mAdView2 = binding.adView2Fragment
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
