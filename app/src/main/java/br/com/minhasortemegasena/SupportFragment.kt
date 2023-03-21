package br.com.minhasortemegasena

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.minhasortemegasena.databinding.SupportFragmentBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupportFragment: Fragment() {

    private lateinit var binding: SupportFragmentBinding
    private lateinit var mAdView: AdView
    private var mInterstitialAd: InterstitialAd? = null
    private val viewModel: SupportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SupportFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBannerAd()
        setupAdInterstitial()
        viewModel.adLoad++
        binding.buttonSendSupportFragment.setOnClickListener {
            val title = binding.textInputEditTitleSupportFragment.text.toString()
            val text = binding.textInputEditTextSupportFragment.text.toString()
            viewModel.uploadSupport(
                title = title,
                text = text,
                context = context
            )
        }
    }

    private fun setupBannerAd() {
        binding.adViewBannerSupportFragment.visibility = View.VISIBLE
        mAdView = binding.adViewBannerSupportFragment
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun setupAdInterstitial() {
        if (viewModel.adLoad>=2){
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                requireContext(),
                "ca-app-pub-3940256099942544/6300978111",
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
                        viewModel.adLoad=0
                    }
                }
            )
        }

    }

}