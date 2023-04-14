package br.com.minhasortemegasena.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.minhasortemegasena.databinding.ItemSortedNumberBinding
import br.com.minhasortemegasena.model.PalpiteModel

class ListNumbersPalpitesAdapter() : ListAdapter<Int, ListNumbersPalpitesAdapter.ViewHolder>(DiffCallback()) {

    private var fullList = mutableListOf<Int>()

    fun updateList(listLotteryModel: List<Int>) {
        fullList = listLotteryModel.toMutableList()
        submitList(fullList)
    }

    class ViewHolder (
        private val binding: ItemSortedNumberBinding
            ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: Int) {
            binding.textViewNumber1ItemSortedNumber.text = currentItem.toString()
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


    class DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Int, newItem: Int) =
            oldItem == newItem
    }

}