package com.cs386p.investment_buddy.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs386p.investment_buddy.R
import com.cs386p.investment_buddy.collections.FoliosData

class FoliosAdapter(private val FolioList: List<FoliosData>) : RecyclerView.Adapter<FoliosAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.folios_row, parent, false)



        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val FolioItem = FolioList[position]

        // sets the text to the textview from our itemHolder class
        holder.nameText.text = FolioItem.name
        //holder.nameText.text = "help"

        holder.itemView.setOnClickListener(){
                Log.d("XXX Folios RV Clicked: ",holder.nameText.text.toString())

        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return FolioList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameFolio)
    }
}