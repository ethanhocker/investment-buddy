package com.cs386p.investment_buddy.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.cs386p.investment_buddy.MainViewModel
import com.cs386p.investment_buddy.R
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.databinding.FragmentAddStockBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date

class AddStockFragment : DialogFragment() {
    private lateinit var binding: FragmentAddStockBinding
    private val viewModel: MainViewModel by viewModels()
    private var onDismissFunction: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this
        return inflater.inflate(R.layout.fragment_add_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddStockBinding.bind(view)

        MainViewModel.fetchFoliosData(FirebaseAuth.getInstance().currentUser!!.uid)

        val adapter = ArrayAdapter(requireContext(),R.layout.foliodropdown_row, arrayListOf<String>())
        var foliosList2: MutableList<FoliosData> = arrayListOf()

        viewModel.observeFoliosData().observe(this){
            val foliosList = viewModel.observeFoliosData().value
            foliosList2 = foliosList!!
            if (foliosList != null) {
                adapter.clear()
                adapter.notifyDataSetChanged()
                for((index, folio) in foliosList.withIndex()){
                    adapter.insert(folio.name, index)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        binding.fakeFolioDropDown.setAdapter(adapter)

        val confirm = binding.confirmFolioBTN
        val args = arguments
        val symbol = args!!.getString("symbol")
        val stockName = args!!.getString("stockName")
        val currentPrice = args!!.getString("currentPrice")

        confirm.setOnClickListener(){
            Log.d("XXX Confirm Button Clicked ", "")
            val dNow = Date()
            val ft = SimpleDateFormat("MM/dd/yyyy")

            var unitsPurchased = 0.0

            if(binding.radioBTN1.isChecked)
            {
                unitsPurchased = binding.amountET.text.toString().toDouble() / currentPrice.toString().toDouble()
            }
            else if(binding.radioBTN2.isChecked)
            {
                unitsPurchased = binding.amountET.text.toString().toDouble()
            }

            val transactionTotal = unitsPurchased * currentPrice!!.toDouble()

            val selectedFolio = binding.fakeFolioDropDown.text.toString()
            var folioPortNumber = ""
            var folioStartingBalance = 0.0
            var folioIndexNumber = 0

            for(n in foliosList2.indices)
            {
                if(foliosList2[n].name == selectedFolio)
                {
                    folioPortNumber = foliosList2[n].port_num
                    folioStartingBalance = foliosList2[n].starting_balance
                    folioIndexNumber = n
                }
            }

            if(transactionTotal <= foliosList2[folioIndexNumber].aab) {
                var holding = HoldingsData(
                    uid = FirebaseAuth.getInstance().currentUser!!.uid,
                    units = unitsPurchased,
                    stock_ticker = symbol!!,
                    stock_name = stockName!!,
                    inception_date = ft.format(dNow),
                    port_num = folioPortNumber,
                    current_price = "",
                    purchase_price = currentPrice
                    )
                viewModel.updateHolding(holding)

                var folio = FoliosData(
                    uid = FirebaseAuth.getInstance().currentUser!!.uid,
                    name = selectedFolio,
                    port_num = folioPortNumber,
                    aab = transactionTotal,
                    starting_balance = folioStartingBalance
                )
                viewModel.updateFolio(folio)
            }
            else
            {
                Toast.makeText(getActivity(), "Insufficient Available Funds to Complete Transaction", Toast.LENGTH_LONG).show();
            }
            dismiss()
        }

    }

    fun setOnDismissFunction(block: () -> Unit){
        onDismissFunction = block
    }
    override fun onDismiss(dialog: DialogInterface) {
        onDismissFunction()
        super.onDismiss(dialog)
    }
}