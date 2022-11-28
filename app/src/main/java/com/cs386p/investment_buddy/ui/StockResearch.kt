package com.cs386p.investment_buddy.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cs386p.investment_buddy.MainViewModel
import com.cs386p.investment_buddy.api.Repository
import com.cs386p.investment_buddy.databinding.ActivityResearchBinding
import com.cs386p.investment_buddy.databinding.ContentResearchBinding

class StockResearch : AppCompatActivity() {
    private lateinit var binding : ContentResearchBinding
    // initialize viewModel
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate layout
        val activityResearchBinding = ActivityResearchBinding.inflate(layoutInflater)
        setContentView(activityResearchBinding.root)
        binding = activityResearchBinding.contentResearch

        // add back button to default supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // grab values from intent
        val symbol = intent.getStringExtra("symbol")
        val stockName = intent.getStringExtra("stockName")
        // set title stock name
        binding.stockName.text = stockName

        // observe quote results and update text on change
        viewModel.observeQuoteResults().observe(this){
            val quote = viewModel.observeQuoteResults().value
            if (quote != null) {
//                binding.currentValue.text = quote.price
            }
        }

        // hit API endpoint based on symbol
        if (symbol != null) {
            viewModel.quoteRequest(symbol)
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