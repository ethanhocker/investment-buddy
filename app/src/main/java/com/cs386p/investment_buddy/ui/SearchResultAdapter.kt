package com.cs386p.investment_buddy.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
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
        // inflate xml
        val stockSearchRowBinding = StockSearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(stockSearchRowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding
        val symbol = currentList[position].symbol
        binding.symbol.text = symbol
        var stockName = currentList[position].name
        // limit length of stock name to 23
        // 22 characters looks good on the Pixel 5. Shouldn't look awful on other devices
        if (stockName.length > 22){
            // if stock name is less than 24, might as well print the whole thing instead of adding "..."
            if (stockName.length > 24){
                stockName = stockName.substring(0, 21).trim() + "..."
            }
        }
        stockName = "($stockName)"
        binding.stockName.text = stockName

        // start stock research activity
        binding.symbol.setOnClickListener {
            val stockResearchClass = StockResearch()
            val intent = Intent(holder.itemView.context, stockResearchClass::class.java)
            intent.putExtra("symbol", symbol)
            intent.putExtra("stockName", currentList[position].name)
            holder.itemView.context.startActivity(intent)
        }
    }

}