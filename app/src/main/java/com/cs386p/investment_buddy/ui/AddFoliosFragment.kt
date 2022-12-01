package com.cs386p.investment_buddy.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.cs386p.investment_buddy.MainViewModel
import com.cs386p.investment_buddy.R
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.databinding.FragmentAddFoliosBinding
import com.google.firebase.auth.FirebaseAuth

class AddFoliosFragment : DialogFragment() {
    private lateinit var binding: FragmentAddFoliosBinding
    private val viewModel: MainViewModel by viewModels()
    private var onDismissFunction: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_folios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddFoliosBinding.bind(view)

        var name = binding.nameET
        var balance = binding.balanceET
        val create = binding.createFolioBTN

        create.setOnClickListener(){
            Log.d("XXX Create Button Clicked ", name.text.toString() + " " + balance.text.toString())
            var folio = FoliosData(
                uid = FirebaseAuth.getInstance().currentUser!!.uid,
                port_num = 11, //TODO figure out how to make this unique
                aab = balance.text.toString().toDouble(),
                name = name.text.toString()
            )
            viewModel.updateFolios(folio)
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