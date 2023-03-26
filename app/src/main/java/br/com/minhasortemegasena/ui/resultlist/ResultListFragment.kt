package br.com.minhasortemegasena.ui.resultlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.databinding.HomeListGamesCaixaFragmentBinding
import br.com.minhasortemegasena.databinding.HomeListResultsCaixaFragmentBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultListFragment: Fragment() {

    private lateinit var binding: HomeListResultsCaixaFragmentBinding
    private lateinit var mAdView: AdView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeListResultsCaixaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardViewItemMegaSena.setOnClickListener {
            findNavController().navigate(R.id.action_list_results_to_megasena_results_fragment)
        }
        binding.cardViewItemLotofacil.setOnClickListener {
            findNavController().navigate(R.id.action_list_results_to_lotofacil_results_fragment)
        }
        binding.cardViewItemFederal.setOnClickListener {
            findNavController().navigate(R.id.action_list_results_to_federal_results_fragment)
        }
        setupAD()
    }

    private fun setupAD() {
        mAdView = binding.adViewBannerSupportFragment
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

}