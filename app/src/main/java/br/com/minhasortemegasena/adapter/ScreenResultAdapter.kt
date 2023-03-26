package br.com.minhasortemegasena.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.minhasortemegasena.databinding.ItemSortedNumberBinding
import br.com.minhasortemegasena.model.LotteryModel

class ScreenResultAdapter() : ListAdapter<String, ScreenResultAdapter.ViewHolder>(DiffCallback()) {

    private var fullList = mutableListOf<String>()

    fun updateList(listLotteryModel: List<String>) {
        fullList = listLotteryModel.toMutableList()
        submitList(fullList)
    }

    class ViewHolder (
        private val binding: ItemSortedNumberBinding
            ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: String) {

                binding.textViewNumber1ItemSortedNumber.text = currentItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSortedNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        return holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem
    }

}