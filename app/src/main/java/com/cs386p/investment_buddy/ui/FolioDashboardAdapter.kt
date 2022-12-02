package com.cs386p.investment_buddy.ui

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.databinding.FoliodashboardRowBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class FolioDashboardAdapter : ListAdapter<HoldingsData, FolioDashboardAdapter.VH>(FolioDashboardDiff()) {
    class FolioDashboardDiff : DiffUtil.ItemCallback<HoldingsData>() {
        override fun areItemsTheSame(oldItem: HoldingsData, newItem: HoldingsData): Boolean {
            return oldItem.stock_ticker == newItem.stock_ticker
        }
        override fun areContentsTheSame(oldItem: HoldingsData, newItem: HoldingsData): Boolean {
            return oldItem.stock_ticker == newItem.stock_ticker
        }
    }

    inner class VH(val binding: FoliodashboardRowBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val foliodashboardRowBinding = FoliodashboardRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(foliodashboardRowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding
        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.HALF_UP

        val totalHoldingsVal = currentList[position].current_price.toDouble() * currentList[position].units
        val rateOfReturn = (currentList[position].current_price.toDouble() - currentList[position].purchase_price.toDouble()) / currentList[position].purchase_price.toDouble() * 100

        binding.tickerFolioDashRV.text = currentList[position].stock_ticker
        binding.stockNameFolioDashRV.text = currentList[position].stock_name
        binding.totalHoldingValueFolioDashRV.text = df.format(totalHoldingsVal).toString()
        binding.personalChangePercentFolioDashRV.text = df.format(rateOfReturn).toString()
        binding.personalChangeDateFolioDashRV.text = currentList[position].inception_date

        if(binding.personalChangePercentFolioDashRV.text.toString().toDouble() > 0)
        {
            binding.personalChangePercentFolioDashRV.setTextColor(Color.GREEN)
            binding.unitFolioDashRV.setTextColor(Color.GREEN)
        }
        else if (binding.personalChangePercentFolioDashRV.text.toString().toDouble() < 0)
        {
            binding.personalChangePercentFolioDashRV.setTextColor(Color.RED)
            binding.unitFolioDashRV.setTextColor(Color.RED)
        }
        else
        {
            binding.personalChangePercentFolioDashRV.setTextColor(Color.WHITE)
            binding.unitFolioDashRV.setTextColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener(){
            Log.d("XXX Holding RV Clicked: ",binding.tickerFolioDashRV.text.toString())
            val stockResearchClass = StockResearch()
            val intent = Intent(holder.itemView.context, stockResearchClass::class.java)
            intent.putExtra("symbol", currentList[position].stock_ticker)
            intent.putExtra("stockName", currentList[position].stock_name)
            holder.itemView.context.startActivity(intent)
        }
    }
}