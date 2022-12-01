package com.cs386p.investment_buddy.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs386p.investment_buddy.collections.FavoritesData
import com.cs386p.investment_buddy.databinding.FavoritesRowBinding

class FavoritesAdapter: ListAdapter<FavoritesData,FavoritesAdapter.VH>(FavoritesDiff()) {
    class FavoritesDiff : DiffUtil.ItemCallback<FavoritesData>() {
        override fun areItemsTheSame(oldItem: FavoritesData, newItem: FavoritesData): Boolean {
            return oldItem.stock_ticker == newItem.stock_ticker
        }

        // TODO: This can be expanded for completeness if more fields are determined to be relevant
        override fun areContentsTheSame(oldItem: FavoritesData, newItem: FavoritesData): Boolean {
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
        binding.stockNameFav.text = currentList[position].stock_name
        binding.changePercentFav.text = "+69.0"
        binding.changeFav.text = "+2.69"

        if(binding.changePercentFav.text.toString().toDouble() > 0)
        {
            binding.changePercentFav.setTextColor(Color.GREEN)
            binding.unitFav.setTextColor(Color.GREEN)
        }
        else if (binding.changePercentFav.text.toString().toDouble() < 0)
        {
            binding.changePercentFav.setTextColor(Color.RED)
            binding.unitFav.setTextColor(Color.RED)
        }
        else
        {
            binding.changePercentFav.setTextColor(Color.WHITE)
            binding.unitFav.setTextColor(Color.WHITE)
        }
    }
}