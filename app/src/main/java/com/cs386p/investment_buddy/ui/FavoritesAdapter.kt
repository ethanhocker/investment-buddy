package com.cs386p.investment_buddy.ui

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs386p.investment_buddy.collections.FavoriteData
import com.cs386p.investment_buddy.databinding.FavoritesRowBinding

class FavoritesAdapter: ListAdapter<FavoriteData,FavoritesAdapter.VH>(FavoritesDiff()) {
    class FavoritesDiff : DiffUtil.ItemCallback<FavoriteData>() {
        override fun areItemsTheSame(oldItem: FavoriteData, newItem: FavoriteData): Boolean {
            return oldItem.stock_ticker == newItem.stock_ticker
        }

        // TODO: This can be expanded for completeness if more fields are determined to be relevant
        override fun areContentsTheSame(oldItem: FavoriteData, newItem: FavoriteData): Boolean {
            return oldItem.stock_ticker == newItem.stock_ticker
        }
    }

    inner class VH(val binding: FavoritesRowBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val favoritesRowBinding = FavoritesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(favoritesRowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding

        // sets the text to the textview from our itemHolder class
        binding.tickerFav.text = currentList[position].stock_ticker
        var stockName = currentList[position].stock_name
        if (stockName.length > 40){
            stockName = stockName.substring(0, 39).trim() + "..."
            binding.stockNameFav.text = stockName
        }
        else{
            binding.stockNameFav.text = stockName
        }
        var percentChange = currentList[position].dailyChange
        binding.changePercentFav.text = percentChange
        var cost = currentList[position].cost
        if (cost != "0") {
            binding.changeFav.text = "$ $cost"
        }

        // set percent change text styling based on value
        if (percentChange != null && percentChange.toDouble() > 0){
            percentChange = "+$percentChange"
            binding.changePercentFav.text = percentChange
            binding.changePercentFav.setTextColor(Color.GREEN)
            binding.unitFav.setTextColor(Color.GREEN)
        }
        else if (percentChange != null && percentChange.toDouble() < 0)
        {
            binding.changePercentFav.setTextColor(Color.RED)
            binding.unitFav.setTextColor(Color.RED)
        }
        else
        {
            binding.changePercentFav.setTextColor(Color.WHITE)
            binding.unitFav.setTextColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener(){
            Log.d("XXX Holding RV Clicked: ",binding.tickerFav.text.toString())
            val stockResearchClass = StockResearch()
            val intent = Intent(holder.itemView.context, stockResearchClass::class.java)
            intent.putExtra("symbol", currentList[position].stock_ticker)
            intent.putExtra("stockName", currentList[position].stock_name)
            holder.itemView.context.startActivity(intent)
        }
    }
}