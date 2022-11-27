package com.cs386p.investment_buddy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs386p.investment_buddy.api.SearchedStock
import com.cs386p.investment_buddy.databinding.StockSearchRowBinding

class SearchResultAdapter : ListAdapter<SearchedStock,SearchResultAdapter.VH>(StockDiff()) {
    class StockDiff : DiffUtil.ItemCallback<SearchedStock>() {
        override fun areItemsTheSame(oldItem: SearchedStock, newItem: SearchedStock): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        // TODO: This can be expanded for completeness if more fields are determined to be relevant
        override fun areContentsTheSame(oldItem: SearchedStock, newItem: SearchedStock): Boolean {
            return oldItem.symbol == newItem.symbol
        }
    }

    inner class VH(val binding: StockSearchRowBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        //inflate xml
        val stockSearchRowBinding = StockSearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(stockSearchRowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding
        binding.symbol.text = currentList[position].symbol
    }

}