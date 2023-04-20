package br.com.minhasortemegasena.ui.games.federal

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
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.databinding.FederalPalpiteFragmentBinding
import br.com.minhasortemegasena.util.Constants.AD_COUNT
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FederalPalpiteFragment : Fragment(), OnUserEarnedRewardListener {

    private lateinit var binding: FederalPalpiteFragmentBinding
    private lateinit var mAdView: AdView
    private lateinit var mAdView2: AdView
    private var rewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"
    private var mInterstitialAd: InterstitialAd? = null
    private val viewModel: FederalPalpiteViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRewardedAd()
        setupAD()
        setupNumberGenerated()
        setupButtonSavePalpite()
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

    private fun setupButtonSavePalpite() {

        binding.buttonSaveSortedNumber.setOnClickListener {
            if (!viewModel.randomNumberList.value.isNullOrEmpty()) {
                setupAdInterstitial()
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Confirmar ação")
                builder.setMessage("Para salvar o palpite as vezes é necessário exibir um anúncio, deseja continuar?")
                builder.setPositiveButton("Sim") { _, _ ->
                    Toast.makeText(requireContext(), "Salvo com sucesso! Veja o seu palpite na aba 'Palpites Salvos' abaixo",
                        Toast.LENGTH_LONG).show()
                    binding.buttonSaveSortedNumber.postDelayed({
                        setupRewardedAd()
                        showRewardAd()
                        viewModel.onSaveEvent()
                    }, 2000)

                }
                builder.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Sorteie um número antes de salvar o palpite",
                    Toast.LENGTH_LONG
                ).show()
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
    }

    private fun showRewardAd() {
        rewardedAd?.let { ad ->
            if (isAdded) {
                ad.show(requireActivity(), OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    Log.d(TAG, "User earned the reward. $rewardAmount, $rewardType")
                })
            } else {
                Log.d(
                    TAG,
                    "The rewarded ad wasn't shown because the Fragment wasn't attached to an activity."
                )
            }
        } ?: run {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }
    }

    private fun setupNumberGenerated() {
        binding.buttonFragmentPalpiteLotofacil.setOnClickListener {
            viewModel.generateRandomNumbers()
            val generatedNumber = viewModel.randomNumberList.value?.first().toString()
            val formattedNumber = String.format("%04d", generatedNumber.toInt())
            binding.textViewFragmentPalpiteFederal.text = formattedNumber
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
        if (AD_COUNT >= 2) {
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
                        AD_COUNT = 0
                    }
                }
            )
        }

    }

    override fun onUserEarnedReward(p0: RewardItem) {

    }


}
