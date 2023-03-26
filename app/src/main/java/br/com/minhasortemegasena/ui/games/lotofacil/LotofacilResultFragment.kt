package br.com.minhasortemegasena.ui.games.lotofacil

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.adapter.ScreenResultAdapter
import br.com.minhasortemegasena.databinding.FragmentLotofacilResultContestBinding
import br.com.minhasortemegasena.databinding.FragmentScreenResultContestBinding
import br.com.minhasortemegasena.util.Constants
import br.com.minhasortemegasena.util.Constants.AD_COUNT
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class LotofacilResultFragment : Fragment() {

    private val viewModel: LotofacilResultViewModel by viewModels()

    private lateinit var binding: FragmentLotofacilResultContestBinding

    private val screenResultAdapter = ScreenResultAdapter()

    private lateinit var mAdView: AdView

    private var contestNumberActual = 0
    private var mInterstitialAd: InterstitialAd? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLotofacilResultContestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupAd()
        setupEditTextContestNumber()
        setupButtonRefresh()
        setupAdInterstitial()
        AD_COUNT++
        viewModel.listLotteryModel.observe(viewLifecycleOwner) {
            if (it.numero!=null && it.acumulado != null && it.valorEstimadoProximoConcurso != null && it.listaDezenas !=null) {
                setContestNumber(it.numero)
                setAccumulated(
                    it.acumulado,
                    it.valorEstimadoProximoConcurso,
                    it.listaRateioPremio.first().valorPremio
                )
                setupDate(it.dataApuracao)
                setupNextDate(it.dataProximoConcurso)
                screenResultAdapter.updateList(it.listaDezenas)
                contestNumberActual = it.numero
            }
        }
        viewModel.getLotteryWithContestNumber()
        binding.toolbarScreenResult.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.status_bar_lotofacil)
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.contestNumberViewModel.isNotBlank()){
            val contestNumberInEditText = viewModel.contestNumberViewModel.toInt()
            if (contestNumberInEditText in 1000 .. contestNumberActual){
                binding.buttonRefreshFragmentMain.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.status_bar_default)
    }

    private fun setupDate(dataApuracao: String) {
        val dateformat = SimpleDateFormat(dataApuracao, Locale.getDefault())
        val date = dateformat.parse(dataApuracao)
        val dateformated = date?.let { dateformat.format(it) }
        binding.textDateFragmentResult.text = dateformated
    }

    private fun setupNextDate(dataProximoConcurso: String) {
        val dateformat = SimpleDateFormat(dataProximoConcurso, Locale.getDefault())
        val date = dateformat.parse(dataProximoConcurso)
        val dateformated = date?.let { dateformat.format(it) }
        binding.textNextDateFragmentResult.text = dateformated
    }

    private fun setAccumulated(
        acumulado: Boolean,
        valorEstimadoProximoConcurso: Double,
        valorDoPremio: Double
    ) = with(binding) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val valorPremioFormatado = numberFormat.format(valorDoPremio)
        val valorEstimadoFormatado = numberFormat.format(valorEstimadoProximoConcurso)
        if (acumulado) {
            titleAccumulatedFragmentResult.text = "Acumulado"
            textAccumulatedFragmentResult.text = valorEstimadoFormatado
        } else {
            titleAccumulatedFragmentResult.text = "Premiação"
            textAccumulatedFragmentResult.text = valorPremioFormatado
        }
    }

    private fun setupRecycler() {
        binding.gridlayoutFragmentScreenResult.apply {
            adapter = screenResultAdapter
            layoutManager = GridLayoutManager(requireContext(), 6)
        }
    }

    private fun setContestNumber(numero: Int) {
        binding.editTextContestNumberFragmentResult.setText(numero.toString())
    }

    private fun setupButtonRefresh() {
        binding.buttonRefreshFragmentMain.apply {
            setOnClickListener {
                viewModel.getLotteryData()
                viewModel.contestNumberViewModel = ""
                this.visibility = View.GONE
            }
        }
    }

    private fun setupEditTextContestNumber() {
        binding.editTextContestNumberFragmentResult.apply {
            this.setOnEditorActionListener { _, i, _ ->
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                    val contestNumber =
                        this.text.toString().toInt()
                    if (contestNumber in 1000..contestNumberActual) {
                        viewModel.contestNumberViewModel = contestNumber.toString()
                        viewModel.getLotteryWithContestNumber()
                        binding.buttonRefreshFragmentMain.visibility = View.VISIBLE
                        this.clearFocus()
                        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                    } else {
                        viewModel.getLotteryData()
                        viewModel.listLotteryModel.observe(viewLifecycleOwner) {
                            viewModel.contestNumberViewModel = it.numero.toString()
                        }
                        Toast.makeText(context, "Insira um número válido", Toast.LENGTH_LONG).show()
                    }
                    true
                } else {
                    false
                }
            }
        }

    }

    private fun setupAd() {
        mAdView = binding.adViewResultFragment
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
