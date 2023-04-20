package br.com.minhasortemegasena.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.minhasortemegasena.databinding.ItemSavedGamesBinding
import br.com.minhasortemegasena.model.PalpiteModel

class ListPalpitesAdapter(val onClickDelete:(PalpiteModel) -> Unit) :
    ListAdapter<PalpiteModel, ListPalpitesAdapter.ViewHolder>(DiffCallback()) {

    private var fullList = mutableListOf<PalpiteModel>()
    private var listNumbersResultMegasena: List<Int> = emptyList()
    private var listNumbersResultLotofacil: List<Int> = emptyList()
    private var listNumbersResultFederal: List<Int> = emptyList()
//    private var listNumbersResultFederal: List<Int> = emptyList()

    fun updateList(listLotteryModel: List<PalpiteModel>) {
        fullList = listLotteryModel.toMutableList()
        submitList(fullList)
    }

    fun updateNumbersResult(numbersResult: List<Int>) {
        listNumbersResultMegasena = numbersResult
        notifyDataSetChanged()
    }

    fun updateNumbersResultLotofacil(numbersResultLotofacil: List<Int>) {
        listNumbersResultLotofacil = numbersResultLotofacil
        notifyDataSetChanged()
    }

    fun updateNumbersResultFederal(numbersResultFederal: List<Int>) {
        listNumbersResultFederal = numbersResultFederal
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemSavedGamesBinding,
        private val onClickDelete: (PalpiteModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentItem: PalpiteModel? = null

        fun bind(
            item: PalpiteModel,
            listNumbersResult: List<Int>,
            listNumbersResultLotofacil: List<Int>,
            listNumbersResultFederal: List<Int>
        ) {
            currentItem = item
            val numberCard = adapterPosition + 1
            val listNumbersPalpitesAdapter = ListNumbersPalpitesAdapter()
            binding.textViewApostaType.text = currentItem?.typeGame
            binding.textViewApostaNumber.text = "#$numberCard"
            setupRecyclerChild(listNumbersPalpitesAdapter)
            listNumbersPalpitesAdapter.updateList(currentItem?.palpiteNumbers ?: emptyList())
            setupButtonCheckResult(listNumbersResult,listNumbersResultLotofacil,listNumbersResultFederal)

            binding.buttonDeletePalpite.setOnClickListener{
                currentItem?.let {
                    onClickDelete(it)
                }
            }
        }

        private fun setupRecyclerChild(listNumbersPalpitesAdapter: ListNumbersPalpitesAdapter) {
            binding.recyclerFragmentPalpite.apply {
                adapter = listNumbersPalpitesAdapter
                layoutManager = GridLayoutManager(context, 6)
            }
        }

        private fun setupButtonCheckResult(
            listNumbersResult: List<Int>,
            listNumbersResultLotofacil: List<Int>,
            listNumbersResultFederal: List<Int>
        ) {

            if (currentItem?.typeGame == "federal"){
                binding.recyclerFragmentPalpite.visibility = View.GONE
                binding.textViewNumberPalpite.visibility = View.VISIBLE
                binding.textViewNumberPalpite.text = currentItem?.palpiteNumbers?.first().toString()
            }else if (currentItem?.typeGame == "megasena"){
                binding.recyclerFragmentPalpite.visibility = View.VISIBLE
                binding.textViewNumberPalpite.visibility = View.GONE
            }else if(currentItem?.typeGame == "lotofacil"){
                binding.recyclerFragmentPalpite.visibility = View.VISIBLE
                binding.textViewNumberPalpite.visibility = View.GONE
            }

            binding.buttonCheckResult.setOnClickListener {
                var countMegasena = 0
                var countLotofacil = 0

                if (currentItem?.typeGame == "megasena"){
                    currentItem?.palpiteNumbers?.forEach { number ->
                        if (number in listNumbersResult) {
                            countMegasena++
                        }
                    }
                    binding.textViewResultChecked.visibility = View.VISIBLE
                    when (countMegasena) {
                        0 -> {
                            binding.textViewResultChecked.text = "Não foi dessa vez!"
                        }
                        1 -> {
                            binding.textViewResultChecked.text = "$countMegasena ACERTO!"
                        }
                        in 2..3 -> {
                            binding.textViewResultChecked.text = "$countMegasena ACERTOS!"
                        }
                        in 4..5 -> {
                            binding.textViewResultChecked.text = "Parabéns, $countMegasena ACERTOS!!"
                        }
                        else -> {
                            binding.textViewResultChecked.text = "VOCÊ É MUITO SORTUDO(A), $countMegasena ACERTOS!!"
                        }
                    }
                } else if(currentItem?.typeGame == "lotofacil"){
                    currentItem?.palpiteNumbers?.forEach { number ->
                        if (number in listNumbersResultLotofacil) {
                            countLotofacil++
                        }
                    }
                    binding.textViewResultChecked.visibility = View.VISIBLE
                    when (countLotofacil) {
                        0 -> {
                            binding.textViewResultChecked.text = "Não foi dessa vez!"
                        }
                        in 1..10 -> {
                            binding.textViewResultChecked.text = "$countLotofacil ACERTOS!"
                        }
                        in 11..14 -> {
                            binding.textViewResultChecked.text = "Parabéns, $countLotofacil ACERTOS!!"
                        }
                        else -> {
                            binding.textViewResultChecked.text = "VOCÊ É MUITO SORTUDO(A), $countLotofacil ACERTOS!!"
                        }
                    }
                }else if(currentItem?.typeGame == "federal"){
                    binding.recyclerFragmentPalpite.visibility = View.GONE
                    binding.textViewNumberPalpite.visibility = View.VISIBLE
                    binding.textViewResultChecked.visibility = View.VISIBLE
                    currentItem?.palpiteNumbers?.forEach { number ->
                        val index = listNumbersResultFederal.indexOf(number)
                        if (index != -1) {
                            // número encontrado em listNumbersResultFederal
                            binding.textViewResultChecked.text = "Parabéns!! Você acertou o número completo no ${index+1}º prêmio"
                        } else {
                            // verificar os três e quatro últimos dígitos
                            val lastThreeDigits = number % 1000
                            val lastFourDigits = number % 10000
                            val indexThreeDigits = listNumbersResultFederal.indexOfFirst { it % 1000 == lastThreeDigits }
                            val indexFourDigits = listNumbersResultFederal.indexOfFirst { it % 10000 == lastFourDigits }
                            if (indexThreeDigits != -1) {
                                // três últimos dígitos encontrados em listNumbersResultFederal
                                binding.textViewResultChecked.text = "Você acertou uma centena no ${indexThreeDigits+1}º prêmio"
                            } else if (indexFourDigits != -1) {
                                // quatro últimos dígitos encontrados em listNumbersResultFederal
                                binding.textViewResultChecked.text = "Você acertou um milhar no ${indexThreeDigits+1}º prêmio"
                            }else{
                                binding.textViewResultChecked.text = "Não foi dessa vez!"
                            }
                        }
                    }
                }
            }
        }

        fun clear() {
            currentItem = null
            binding.textViewResultChecked.visibility = View.GONE
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSavedGamesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClickDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.clear()
        holder.bind(currentItem, listNumbersResultMegasena, listNumbersResultLotofacil, listNumbersResultFederal)
    }

    class DiffCallback : DiffUtil.ItemCallback<PalpiteModel>() {
        override fun areItemsTheSame(oldItem: PalpiteModel, newItem: PalpiteModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PalpiteModel, newItem: PalpiteModel) =
            oldItem == newItem
    }

}
