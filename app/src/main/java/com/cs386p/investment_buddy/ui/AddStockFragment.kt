package com.cs386p.investment_buddy.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

        //val folios = resources.getStringArray(R.array.food)
        MainViewModel.fetchFoliosData(FirebaseAuth.getInstance().currentUser!!.uid)

        val adapter = ArrayAdapter(requireContext(),R.layout.foliodropdown_row, arrayListOf<String>())

        viewModel.observeFoliosData().observe(this){
            val foliosList = viewModel.observeFoliosData().value
            if (foliosList != null) {
                println("\n\n\n\n***************************************folios list len: " + foliosList.size)
            }
            if (foliosList != null) {
                adapter.clear()
                adapter.notifyDataSetChanged()
                var i = 0
                for(folio in foliosList){
                    adapter.insert(folio.name, i)
                    adapter.notifyDataSetChanged()
                    i++
                }
            }
        }

//        var foliosList = viewModel.observeFoliosData()
//        println("*********************** " + foliosList.value.toString())
//        var foliosArray = arrayOf<String>()
//
//        for(folio in foliosList.value!!){
//            foliosArray += folio.name
//        }


        binding.fakeFolioDropDown.setAdapter(adapter)

        val confirm = binding.confirmFolioBTN

        confirm.setOnClickListener(){
            Log.d("XXX Confirm Button Clicked ", "")
            val dNow = Date()
            val ft = SimpleDateFormat("MM/dd/yyyy")

            println(ft.format(dNow))


            var holding = HoldingsData(
                uid = FirebaseAuth.getInstance().currentUser!!.uid,
                units = binding.amountET.text.toString().toDouble(),
                stock_ticker = "AMD",
                stock_name = "Advanced Micro Devices",
                inception_date = ft.format(dNow),
                port_num = "69", //TODO

            )
            viewModel.updateHolding(holding)
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