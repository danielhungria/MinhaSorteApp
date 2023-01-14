package br.com.minhasortemegasena.ui.resultscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.minhasortemegasena.databinding.FragmentScreenResultContestBinding
import com.google.android.gms.ads.AdView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ScreenResultContestsFragment : Fragment(){

    private val viewModel: ScreenResultViewModel by viewModels()

    private lateinit var binding: FragmentScreenResultContestBinding

    private lateinit var mAdView: AdView



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLotteryData()
        viewModel.listLotteryModel.observe(viewLifecycleOwner) {
            setContestNumber(it.numero)
            setSortedNumbers(it.listaDezenas)
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
        binding.contestNumberFragmentResult.setText(numero.toString())
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