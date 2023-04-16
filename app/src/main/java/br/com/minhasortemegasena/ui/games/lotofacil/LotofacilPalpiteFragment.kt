package br.com.minhasortemegasena.ui.games.lotofacil

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.adapter.MainAdapter
import br.com.minhasortemegasena.databinding.LotofacilPalpiteFragmentBinding
import br.com.minhasortemegasena.util.Constants
import br.com.minhasortemegasena.util.Constants.AD_COUNT
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LotofacilPalpiteFragment : Fragment() {

    private lateinit var binding: LotofacilPalpiteFragmentBinding
    private lateinit var mAdView: AdView
    private lateinit var mAdView2: AdView
    private val viewModel: LotofacilPalpiteViewModel by viewModels()
    private val mainAdapter = MainAdapter()
    private var sliderValue = 15
    private var mInterstitialAd: InterstitialAd? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSlider()
        setupAD()
        setupRecycler()
        setupAdInterstitial()
        setupButtonGenerateNumbers()
        setupButtonSavePalpite()
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.status_bar_lotofacil)
        binding.toolbarLotofacilPalpite.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
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

    override fun onResume() {
        super.onResume()
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.status_bar_lotofacil)
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
        binding.sliderFragmentPalpiteLotofacil.addOnChangeListener { slider, value, fromUser ->
            val sliderN = value.toInt()
            sliderValue = sliderN
        }

    }

    private fun setupRecycler() {
        binding.gridlayoutFragmentPalpiteLotofacil.apply {
            adapter = mainAdapter.apply {
                viewModel.submitList(mainAdapter)
            }
            layoutManager = GridLayoutManager(requireContext(), 6).apply {

            }
        }
    }

    private fun setupButtonGenerateNumbers() {
        binding.buttonFragmentPalpiteLotofacil.setOnClickListener {
            viewModel.generateRandomNumbers(sliderValue)
            viewModel.submitList(mainAdapter)
            binding.scrollViewFragment.post {
                binding.scrollViewFragment.fullScroll(ScrollView.FOCUS_DOWN)
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

    private fun setupAdInterstitial() {
        if (AD_COUNT >=2){
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
