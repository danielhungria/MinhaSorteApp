package br.com.minhasortemegasena.ui.savedgames

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.adapter.ListPalpitesAdapter
import br.com.minhasortemegasena.databinding.HomeSavedGamesFragmentBinding
import br.com.minhasortemegasena.util.Constants
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeSavedGamesFragment: Fragment() {

    private lateinit var binding: HomeSavedGamesFragmentBinding
    private lateinit var mAdView: AdView
    private lateinit var mAdView2: AdView
    private val viewModel: HomeSavedGamesViewModel by viewModels()
    private var mInterstitialAd: InterstitialAd? = null
    private val listPalpitesAdapter = ListPalpitesAdapter(onClickDelete = {
        viewModel.deleteItem(it)
    })


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupAdInterstitial()
        viewModel.getLotteryDataMegasena()
        viewModel.getLotteryDataLotofacil()
        viewModel.getLotteryDataFederal()
        viewModel.listLotteryModel.observe(viewLifecycleOwner){
            it.listaDezenas?.map { it.toInt() }?.let { listNumbersResult ->
                listPalpitesAdapter.updateNumbersResult(listNumbersResult)
            }
        }
        viewModel.listLotteryModelLotofacil.observe(viewLifecycleOwner){
            it.listaDezenas?.map { it.toInt() }?.let { listNumbersResult ->
                listPalpitesAdapter.updateNumbersResultLotofacil(listNumbersResult)
            }
        }
        viewModel.listLotteryModelFederal.observe(viewLifecycleOwner){
            it.listaDezenas?.map { it.toInt() }?.let { listNumbersResult ->
                listPalpitesAdapter.updateNumbersResultFederal(listNumbersResult)
            }
        }
        viewModel.palpiteModel.observe(viewLifecycleOwner){
            listPalpitesAdapter.updateList(it)
        }
        viewModel.fetchItemList()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeSavedGamesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }



    private fun setupRecycler() {
        binding.recyclerViewFragment.apply {
            adapter = listPalpitesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    private fun setupAdInterstitial() {
        if (Constants.AD_COUNT >=2){
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
                        Constants.AD_COUNT =0
                    }
                }
            )
        }

    }



}