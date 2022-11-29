package com.cs386p.investment_buddy.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs386p.investment_buddy.R
import com.cs386p.investment_buddy.collections.HoldingsData

class FolioDashboardAdapter(private val HoldingsList: List<HoldingsData>) : RecyclerView.Adapter<FolioDashboardAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.foliodashboard_row, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val HoldingItem = HoldingsList[position]

        // sets the text to the textview from our itemHolder class
        holder.tickerText.text = HoldingItem.stock_ticker
        holder.stockNameText.text = HoldingItem.stock_name
        holder.holdingValueText.text = "$15,000.00"
        holder.personalChangePercentText.text = "+6.69"
        holder.inceptionDateText.text = HoldingItem.inception_date

        if(holder.personalChangePercentText.text.toString().toDouble() > 0)
        {
            holder.personalChangePercentText.setTextColor(Color.GREEN)
            holder.unitText.setTextColor(Color.GREEN)
        }
        else if (holder.personalChangePercentText.text.toString().toDouble() < 0)
        {
            holder.personalChangePercentText.setTextColor(Color.RED)
            holder.unitText.setTextColor(Color.RED)
        }
        else
        {
            holder.personalChangePercentText.setTextColor(Color.WHITE)
            holder.unitText.setTextColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener(){
            Log.d("XXX Holding RV Clicked: ",holder.tickerText.text.toString())
            //TODO: Look into using this for sell (true or false)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return HoldingsList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tickerText: TextView = itemView.findViewById(R.id.tickerFolioDashRV)
        val stockNameText: TextView = itemView.findViewById(R.id.stockNameFolioDashRV)
        val holdingValueText: TextView = itemView.findViewById(R.id.totalHoldingValueFolioDashRV)
        val personalChangePercentText: TextView = itemView.findViewById(R.id.personalChangePercentFolioDashRV)
        val unitText: TextView = itemView.findViewById(R.id.unitFolioDashRV)
        val inceptionDateText: TextView = itemView.findViewById(R.id.personalChangeDateFolioDashRV)
    }
}