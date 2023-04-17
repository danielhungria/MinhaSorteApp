package br.com.minhasortemegasena.ui.games.megasena

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.adapter.LotofacilResultAdapter
import br.com.minhasortemegasena.adapter.ScreenResultAdapter
import br.com.minhasortemegasena.databinding.FragmentScreenResultContestBinding
import br.com.minhasortemegasena.model.ResultFederalModel
import br.com.minhasortemegasena.util.Constants.AD_COUNT
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ScreenResultContestsFragment : Fragment() {

    private val viewModel: ScreenResultViewModel by viewModels()

    private lateinit var binding: FragmentScreenResultContestBinding

    private val screenResultAdapter = ScreenResultAdapter()

    private val lotofacilResultAdapter = LotofacilResultAdapter()

    private lateinit var mAdView: AdView

    private var contestNumberActual = 0
    private var mInterstitialAd: InterstitialAd? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreenResultContestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AD_COUNT++
        setupRecyclerDetailResult()
        setupRecycler()
        setupAd()
        setupEditTextContestNumber()
        setupButtonRefresh()
        setupAdInterstitial()
        setupButtonSearchContestNumber()
        viewModel.listLotteryModel.observe(viewLifecycleOwner) {
            if (it.numero!=null && it.acumulado != null && it.valorEstimadoProximoConcurso != null && it.listaDezenas !=null && it.listaRateioPremio!=null) {
                setContestNumber(it.numero)
                setAccumulated(
                    it.acumulado,
                    it.valorEstimadoProximoConcurso,
                    it.listaRateioPremio.first()
                )
                setupDate(it.dataApuracao)
                setupNextDate(it.dataProximoConcurso)
                screenResultAdapter.updateList(it.listaDezenas)
                contestNumberActual = it.numero
            }
        }
        viewModel.listaRateioPremio.observe(viewLifecycleOwner){
            lotofacilResultAdapter.updateList(it)
        }
        viewModel.listLastLotteryModel.observe(viewLifecycleOwner){
            if (it.numero!=null) viewModel.lastContestNumberViewModel = it.numero
        }
        viewModel.getLotteryWithContestNumber()
        viewModel.getLastLotteryData()
        binding.toolbarScreenResult.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
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

    private fun setupDate(dataApuracao: String) {
        val dateformat = SimpleDateFormat(dataApuracao, Locale.getDefault())
        val date = dateformat.parse(dataApuracao)
        val dateformated = date?.let { dateformat.format(it) }
        binding.textViewContestDate.text = dateformated
    }

    private fun setupNextDate(dataProximoConcurso: String) {
        val dateformat = SimpleDateFormat(dataProximoConcurso, Locale.getDefault())
        val date = dateformat.parse(dataProximoConcurso)
        val dateformated = date?.let { dateformat.format(it) }
        binding.textViewNextContestDate.text = dateformated
    }

    private fun setAccumulated(
        acumulado: Boolean,
        valorEstimadoProximoConcurso: Double,
        listaRateio: ResultFederalModel
    ) = with(binding) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val valorEstimadoFormatado = numberFormat.format(valorEstimadoProximoConcurso)
        if (acumulado) {
            textViewAccumulated.text = "ACUMULOU!"
            textViewAccumulatedReward.text = valorEstimadoFormatado
        } else {
            when(listaRateio.numeroDeGanhadores){
                1 -> {
                    textViewAccumulated.text = "${listaRateio.numeroDeGanhadores} GANHADOR"
                }
                else -> {
                    textViewAccumulated.text = "${listaRateio.numeroDeGanhadores} GANHADORES"
                }
            }

            textViewAccumulatedReward.text = valorEstimadoFormatado
        }
    }

    private fun setupRecycler() {
        binding.gridlayoutFragmentScreenResult.apply {
            adapter = screenResultAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }
    private fun setupRecyclerDetailResult() {
        binding.recyclerResult.apply {
            adapter = lotofacilResultAdapter
            layoutManager = LinearLayoutManager(requireContext())
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

    private fun setupButtonSearchContestNumber() {
        binding.buttonPreviousContest.setOnClickListener {
            val contestNumber =
                binding.editTextContestNumberFragmentResult.text.toString().toInt()
            val newContestNumber = contestNumber - 1
            if (contestNumber in 1000..contestNumberActual && newContestNumber <= contestNumberActual) {
                viewModel.contestNumberViewModel = newContestNumber.toString()
                viewModel.getLotteryWithContestNumber()
                binding.buttonRefreshFragmentMain.visibility = View.VISIBLE
                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                binding.editTextContestNumberFragmentResult.setText(contestNumber.toString())
            } else {
                viewModel.getLotteryData()
                viewModel.listLotteryModel.observe(viewLifecycleOwner) {
                    viewModel.contestNumberViewModel = it.numero.toString()
                }
                Toast.makeText(context, "Insira um número válido", Toast.LENGTH_LONG).show()
            }

        }

        binding.buttonNextContest.setOnClickListener {
            val contestNumber =
                binding.editTextContestNumberFragmentResult.text.toString().toInt()
            val newContestNumber = contestNumber + 1
            val t = viewModel.lastContestNumberViewModel
            if (newContestNumber in 1000..viewModel.lastContestNumberViewModel && newContestNumber <= viewModel.lastContestNumberViewModel) {
                viewModel.contestNumberViewModel = newContestNumber.toString()
                viewModel.getLotteryWithContestNumber()

                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                binding.editTextContestNumberFragmentResult.setText(contestNumber.toString())

                when(newContestNumber==viewModel.lastContestNumberViewModel){
                    true -> {
                        binding.buttonRefreshFragmentMain.visibility = View.GONE
                    }
                    else -> {
                        binding.buttonRefreshFragmentMain.visibility = View.VISIBLE
                    }
                }

            } else {
                viewModel.getLotteryData()
                viewModel.listLotteryModel.observe(viewLifecycleOwner) {
                    viewModel.contestNumberViewModel = it.numero.toString()
                }
                Toast.makeText(context, "Insira um número válido", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setupAd() {
//        mAdView = binding.adViewResultFragment
        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)
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