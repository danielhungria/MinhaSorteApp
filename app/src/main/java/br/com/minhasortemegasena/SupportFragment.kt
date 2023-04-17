package br.com.minhasortemegasena

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.minhasortemegasena.databinding.SupportFragmentBinding
import br.com.minhasortemegasena.util.Constants.AD_COUNT
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
        AD_COUNT++
//        binding.buttonSendSupportFragment.text = "Enviar"
        binding.buttonSendSupportFragment.setOnClickListener {
            val title = binding.textInputEditTitleSupportFragment.text.toString()
            val text = binding.textInputEditTextSupportFragment.text.toString()
            viewModel.uploadSupport(
                title = title,
                text = text,
                context = context
            ) { success ->
                if (success) {
                    binding.buttonSendSupportFragment.text = "Enviado!"
                    binding.textInputEditTitleSupportFragment.text?.clear()
                    binding.textInputEditTextSupportFragment.text?.clear()
                    val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
                } else {
                    binding.buttonSendSupportFragment.text = "Serviço Indisponível"
                }
            }
        }
    }

    private fun setupBannerAd() {
        binding.adViewBannerSupportFragment.visibility = View.VISIBLE
        mAdView = binding.adViewBannerSupportFragment
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
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