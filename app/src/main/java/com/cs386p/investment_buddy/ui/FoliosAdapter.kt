package com.cs386p.investment_buddy.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.databinding.FoliosRowBinding

import androidx.recyclerview.widget.ListAdapter
import com.cs386p.investment_buddy.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class FoliosAdapter: ListAdapter<FoliosData,FoliosAdapter.VH>(FoliosDiff()) {
    class FoliosDiff : DiffUtil.ItemCallback<FoliosData>() {
        override fun areItemsTheSame(oldItem: FoliosData, newItem: FoliosData): Boolean {
            return oldItem.port_num == newItem.port_num
        }
        override fun areContentsTheSame(oldItem: FoliosData, newItem: FoliosData): Boolean {
            return oldItem.port_num == newItem.port_num
        }
    }

    inner class VH(val binding: FoliosRowBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        // inflates the card_view_design view
        // that is used to hold list item
        val foliosRowBinding = FoliosRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(foliosRowBinding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding
        val user = FirebaseAuth.getInstance().currentUser!!.uid

        // sets the text to the textview from our itemHolder class
        binding.nameFolio.text = currentList[position].name

        binding.nameFolio.setOnClickListener(){
            Log.d("XXX Folios RV Clicked: ",binding.nameFolio.text.toString())
            val folioDashboardClass = FolioDashboard()
            val intent = Intent(holder.itemView.context, folioDashboardClass::class.java)
            intent.putExtra("folioName", binding.nameFolio.text.toString())
            intent.putExtra("folioPortNum",currentList[position].port_num)
            intent.putExtra("folioStartingBalance",currentList[position].starting_balance.toString())
            intent.putExtra("folioAAB", currentList[position].aab.toString())
            holder.itemView.context.startActivity(intent)
        }

        binding.deleteFolioBTN.setOnClickListener(){
            val folioName = binding.nameFolio.text.toString()
            Log.d("XXX Folios RV Delete Icon Clicked: ", folioName)
            MainViewModel.deleteFolioAndHoldings(user, currentList[position].port_num)
        }
    }
}