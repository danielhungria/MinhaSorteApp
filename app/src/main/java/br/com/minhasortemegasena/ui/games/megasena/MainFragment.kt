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
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), OnUserEarnedRewardListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var mAdView: AdView
    private lateinit var mAdView2: AdView
    private var rewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"
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
            if (!viewModel.randomNumberList.value.isNullOrEmpty()){
                setupAdInterstitial()
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirmar ação")
                builder.setMessage("Para salvar o palpite as vezes é necessário exibir um anúncio, deseja continuar?")
                builder.setPositiveButton("Sim") { _, _ ->
                    Toast.makeText(requireContext(), "Salvo com sucesso! Veja o seu palpite na aba 'Palpites Salvos' abaixo",
                        Toast.LENGTH_SHORT).show()
                    binding.buttonSaveSortedNumber.postDelayed({
                        setupRewardedAd()
                        viewModel.onSaveEvent()

                    }, 2000)
                }
                builder.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show()
            }else{
                Toast.makeText(requireContext(), "Sorteie um número antes de salvar o palpite",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            requireContext(),
            getString(R.string.ad_view_rewarded),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { it1 -> Log.d(TAG, it1) }
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }
            })

        rewardedAd?.let { ad ->
            if (isAdded) {
                ad.show(requireActivity(), OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    Log.d(TAG, "User earned the reward. $rewardAmount, $rewardType")
                })
            } else {
                Log.d(TAG, "The rewarded ad wasn't shown because the Fragment wasn't attached to an activity.")
            }
        } ?: run {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
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

    override fun onUserEarnedReward(p0: RewardItem) {

    }
}