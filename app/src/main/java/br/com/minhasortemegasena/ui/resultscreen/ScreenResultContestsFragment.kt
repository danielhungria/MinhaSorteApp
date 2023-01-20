package br.com.minhasortemegasena.ui.resultscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.minhasortemegasena.databinding.FragmentScreenResultContestBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ScreenResultContestsFragment : Fragment(){

    private var mInterstitialAds: InterstitialAd? = null

    private val viewModel: ScreenResultViewModel by viewModels()

    private lateinit var binding: FragmentScreenResultContestBinding

    private lateinit var mAdView: AdView


    private fun setupDate(dataApuracao: String) {
        val dateformat = SimpleDateFormat(dataApuracao, Locale.getDefault())
        val date = dateformat.parse(dataApuracao)
        val dateformated = date?.let { dateformat.format(it) }
        binding.contestDataFragmentResult.text = dateformated
    }

    private fun setAccumulated(acumulado: Boolean) = with(binding) {
        if (acumulado){
            resultAccumulatedFragmentResult.text = "Acumulou"
        }else{
            resultAccumulatedFragmentResult.text = "Não Acumulou"
        }
    }

    private fun setSortedNumbers(listaDezenas: List<String>) {
        binding.textViewNumber1FragmentScreenResult.text = listaDezenas[0]
        binding.textViewNumber2FragmentScreenResult.text = listaDezenas[1]
        binding.textViewNumber3FragmentScreenResult.text = listaDezenas[2]
        binding.textViewNumber4FragmentScreenResult.text = listaDezenas[3]
        binding.textViewNumber5FragmentScreenResult.text = listaDezenas[4]
        binding.textViewNumber6FragmentScreenResult.text = listaDezenas[5]
    }

    private fun setContestNumber(numero: Int) {
        if (numero != null){
            binding.contestNumberFragmentResult.setText(numero.toString())
        }
    }

    private fun setupAdInterstitialScreen(adRequest: AdRequest) {
        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-1398509773631594/9102859623",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d("Fragment", it) }
                    mInterstitialAds = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("Fragment", "Ad was loaded.")
                    mInterstitialAds = interstitialAd
                    mInterstitialAds?.show(requireActivity())
                }
            }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdView = binding.adViewResultFragment
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        viewModel.getLotteryData()
        setupAdInterstitialScreen(adRequest)
        viewModel.listLotteryModel.observe(viewLifecycleOwner) {
            it.numero?.let { numero -> setContestNumber(numero) }
            it.listaDezenas?.let { listadezenas -> setSortedNumbers(listadezenas) }
            it.acumulado?.let { acumulado -> setAccumulated(acumulado) }
            it.dataApuracao?.let { dataApuracao -> setupDate(dataApuracao) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreenResultContestBinding.inflate(inflater, container, false)
        return binding.root
    }

}
