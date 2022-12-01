package com.cs386p.investment_buddy.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.cs386p.investment_buddy.MainViewModel
import com.cs386p.investment_buddy.api.Repository
import com.cs386p.investment_buddy.collections.FavoritesData
import com.cs386p.investment_buddy.databinding.ActivityResearchBinding
import com.cs386p.investment_buddy.databinding.ContentResearchBinding
import com.google.firebase.auth.FirebaseAuth

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

        val user = FirebaseAuth.getInstance().currentUser!!.uid
        var favoritesList = viewModel.observeFavoritesData()

        // grab values from intent
        val symbol = intent.getStringExtra("symbol").toString()
        val stockName = intent.getStringExtra("stockName").toString()
        // set title stock name
        binding.stockName.text = stockName

        checkFavList(stockName, favoritesList)

        // observe quote results and update text on change
        viewModel.observeFinnhubQuoteResults().observe(this){
            val quote = viewModel.observeFinnhubQuoteResults().value
            if (quote != null) {
                binding.currentValue.text = quote.currentPrice
            }
        }

        // hit quote API endpoint based on symbol
        if (symbol != null) {
            viewModel.finnhubQuoteRequest(symbol)
        }

        val fragmentManager = (this as AppCompatActivity).supportFragmentManager
        binding.addButton.setOnClickListener(){
            val addFolioPopUp = AddStockFragment()
            addFolioPopUp.show(fragmentManager,"Purchase Stock")

            /*val intent = Intent(this, AddStockFragment::class.java)
            intent.putExtra("symbol", symbol)
            intent.putExtra("stockName", stockName)*/
        }

        binding.favoriteButton.setOnClickListener(){
            var fav = FavoritesData(
                uid = user,
                stock_ticker = symbol,
                stock_name = stockName,

            )

            viewModel.updateFavorites(fav)
            //TODO figure out how to update this live
            favoritesList = viewModel.observeFavoritesData()
            checkFavList(stockName, favoritesList)
        }
    }

    // end activity - can add more logic here if needed
    private fun doFinish(){
        finish()
    }

    private fun checkFavList(stockName: String, favoritesList: MutableLiveData<MutableList<FavoritesData>>){
        for(fav in favoritesList.value!!){
            if(stockName == fav.stock_name)
            {
                binding.favoriteButton.text = "UNFAVORITE"
                break
            }
            else
            {
                binding.favoriteButton.text = "FAVORITE"
            }
        }
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