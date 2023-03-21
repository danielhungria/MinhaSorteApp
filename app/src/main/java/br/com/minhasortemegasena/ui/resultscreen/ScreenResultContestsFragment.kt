package br.com.minhasortemegasena.ui.resultscreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.adapter.ScreenResultAdapter
import br.com.minhasortemegasena.databinding.FragmentScreenResultContestBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ScreenResultContestsFragment : Fragment() {

    private val viewModel: ScreenResultViewModel by viewModels()

    private lateinit var binding: FragmentScreenResultContestBinding

    private val screenResultAdapter = ScreenResultAdapter()

    private lateinit var mAdView: AdView

    private var contestNumberActual = 0


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
        setupRecycler()
        setupAd()
        setupEditTextContestNumber()
        setupButtonRefresh()
        viewModel.listLotteryModel.observe(viewLifecycleOwner) {
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
        viewModel.getLotteryWithContestNumber()
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
            layoutManager = GridLayoutManager(requireContext(), 3)
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
}
