package com.cs386p.investment_buddy.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs386p.investment_buddy.MainViewModel
import com.cs386p.investment_buddy.databinding.ActivitySearchBinding
import com.cs386p.investment_buddy.databinding.ContentSearchBinding

class StockSearch : AppCompatActivity() {
    private lateinit var binding : ContentSearchBinding
    // initialize viewModel
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate layout
         val activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(activitySearchBinding.root)
        binding = activitySearchBinding.contentSearch

        // add back button to default supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TODO: possibly set a button to do this in the future as well
        binding.actionSearch.setOnFocusChangeListener{ _, hasFocus ->
            val symbol = binding.actionSearch.text
            if (symbol.isNotEmpty()){
                viewModel.symbolSearch(binding.actionSearch.text.toString())
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.stockSearchRecyclerView.layoutManager = layoutManager
        val adapter = SearchResultAdapter()
        binding.stockSearchRecyclerView.adapter = adapter

        viewModel.observeSearchResults().observe(this){
            val results = viewModel.observeSearchResults().value
            adapter.submitList(results)
        }
    }

    // end activity - can add more logic here if needed
    private fun doFinish(){
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == android.R.id.home) {
            // If user clicks "up", then it it as if they clicked OK.  We commit
            // changes and return to parent
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }
}