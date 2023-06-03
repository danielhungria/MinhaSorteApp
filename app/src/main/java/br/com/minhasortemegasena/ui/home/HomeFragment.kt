package br.com.minhasortemegasena.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.databinding.HomeListGamesCaixaFragmentBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var binding: HomeListGamesCaixaFragmentBinding
    private lateinit var mAdView: AdView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeListGamesCaixaFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardViewItemMegaSena.setOnClickListener {
            findNavController().navigate(R.id.action_home_list_games_to_megasena_palpites_fragment)
        }
        binding.cardViewItemLotofacil.setOnClickListener {
            findNavController().navigate(R.id.action_home_list_games_to_lotofacil_palpites_fragment)
        }
        binding.cardViewItemFederal.setOnClickListener {
            findNavController().navigate(R.id.action_home_list_games_to_federal_palpites_fragment)
        }
        binding.cardViewItemQuina.setOnClickListener {
            findNavController().navigate(R.id.action_home_list_games_to_quina_palpites_fragment)
        }
        setupAD()
    }

    private fun setupAD() {
        mAdView = binding.adViewBannerSupportFragment
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

}