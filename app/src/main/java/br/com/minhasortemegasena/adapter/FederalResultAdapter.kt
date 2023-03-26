package br.com.minhasortemegasena.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.minhasortemegasena.R
import br.com.minhasortemegasena.databinding.ItemListFederalResultBinding
import br.com.minhasortemegasena.databinding.ItemSortedNumberBinding
import br.com.minhasortemegasena.model.ListaRateioPremio
import br.com.minhasortemegasena.model.LotteryModel
import br.com.minhasortemegasena.model.ResultFederalModel
import java.text.NumberFormat
import java.util.*

class FederalResultAdapter() : ListAdapter<ResultFederalModel, FederalResultAdapter.ViewHolder>(DiffCallback()) {

    private var fullList = mutableListOf<ResultFederalModel>()

    fun updateList(listLotteryModel: List<ResultFederalModel>) {
        fullList = listLotteryModel.toMutableList()
        submitList(fullList)
    }

    class ViewHolder (
        private val binding: ItemListFederalResultBinding
            ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: ResultFederalModel) {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            val numberFormatted =  numberFormat.format(currentItem.valorPremio)
            binding.titlePremioContestFragmentResult.text = numberFormatted.toString()
            binding.titlePositionContestFragmentResult.text = currentItem.faixa.toString()
            binding.titleBilheteContestFragmentResult.text = currentItem.listaDezenas[adapterPosition]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListFederalResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        return holder.bind(currentItem)
    }



    class DiffCallback : DiffUtil.ItemCallback<ResultFederalModel>() {
        override fun areItemsTheSame(oldItem: ResultFederalModel, newItem: ResultFederalModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ResultFederalModel, newItem: ResultFederalModel) =
            oldItem == newItem
    }

}