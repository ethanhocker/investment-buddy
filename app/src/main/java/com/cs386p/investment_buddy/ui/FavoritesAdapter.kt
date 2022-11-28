package com.cs386p.investment_buddy.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs386p.investment_buddy.R
import com.cs386p.investment_buddy.collections.FavoritesData

class FavoritesAdapter(private val FavList: List<FavoritesData>) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_row, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val FavItem = FavList[position]

        // sets the text to the textview from our itemHolder class
        holder.tickerText.text = FavItem.stock_ticker
        holder.stockNameText.text = FavItem.stock_name
        holder.changePercentText.text = "+69.0"
        holder.changeText.text = "+2.69"

        if(holder.changePercentText.text.toString().toDouble() > 0)
        {
            holder.changePercentText.setTextColor(Color.GREEN)
            holder.unitText.setTextColor(Color.GREEN)
        }
        else if (holder.changePercentText.text.toString().toDouble() < 0)
        {
            holder.changePercentText.setTextColor(Color.RED)
            holder.unitText.setTextColor(Color.RED)
        }
        else
        {
            holder.changePercentText.setTextColor(Color.WHITE)
            holder.unitText.setTextColor(Color.WHITE)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return FavList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tickerText: TextView = itemView.findViewById(R.id.tickerFav)
        val stockNameText: TextView = itemView.findViewById(R.id.stockNameFav)
        val changePercentText: TextView = itemView.findViewById(R.id.changePercentFav)
        val unitText: TextView = itemView.findViewById(R.id.unitFav)
        val changeText: TextView = itemView.findViewById(R.id.changeFav)
    }
}