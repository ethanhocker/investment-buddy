package com.cs386p.investment_buddy.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cs386p.investment_buddy.MainViewModel
import com.cs386p.investment_buddy.collections.FavoriteData
import com.cs386p.investment_buddy.databinding.ActivityResearchBinding
import com.cs386p.investment_buddy.databinding.ContentResearchBinding
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StockResearch : AppCompatActivity() {
    private lateinit var binding : ContentResearchBinding
    private val viewModel: MainViewModel by viewModels()
    private var localFavoritesDataList = mutableListOf<FavoriteData>()

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate layout
        val activityResearchBinding = ActivityResearchBinding.inflate(layoutInflater)
        setContentView(activityResearchBinding.root)
        binding = activityResearchBinding.contentResearch

        // add back button to default supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user = FirebaseAuth.getInstance().currentUser!!.uid

        // grab values from intent
        val symbol = intent.getStringExtra("symbol").toString()
        val stockName = intent.getStringExtra("stockName").toString()
        var currentPrice = ""
        // set title stock name
        binding.stockName.text = stockName

        MainViewModel.fetchFavoritesData(FirebaseAuth.getInstance().currentUser!!.uid)

        viewModel.observeFavoriteDataList().observe(this) {
            var favoritesDataList = viewModel.observeFavoriteDataList().value
            if (favoritesDataList != null) {
                for (favoritesData in favoritesDataList){
                    checkFavList(stockName, favoritesDataList)
                    localFavoritesDataList.add(favoritesData)
                }
            }
        }

        // add popup for analyzing stocks info
        binding.insiderSentimentInfo.setOnClickListener {
            // inflate the layout of the popup window
            val popupView = LayoutInflater.from(this).inflate(
                com.cs386p.investment_buddy.R.layout.popup_window,
                binding.root,
                false
            )
            val infoTextView = popupView.findViewById<TextView>(com.cs386p.investment_buddy.R.id.infoText)
            infoTextView.text = "Insider Sentiment rating is based on what trades executives of the company are making in the most recent calculation period." +
                    "The rating is based on Finnhub's monthly share purchase ratio with the ratings as follows: " +
                    "Strong Buy > 75, Buy > 25, 25 > Hold > -25, Sell < -25, Strong Sell < -75. See Finnhub's documentation for more info."

            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // show the popup window
            popupWindow.showAtLocation(binding.insiderSentimentInfo, Gravity.CENTER, 0, 0)

            // dismiss the popup window when touched
            popupView.setOnTouchListener { v, event ->
                popupWindow.dismiss()
                true
            }
        }

        // observe quote results and update text views on change
        viewModel.observeFinnhubQuoteResults().observe(this){
            val quote = viewModel.observeFinnhubQuoteResults().value
            if (quote != null) {
                currentPrice = quote.currentPrice
                binding.currentValue.text = "$ " + currentPrice
                var percentChange = quote.percentChange
                // set percent change text styling based on value
                if (percentChange != null && percentChange.toDouble() > 0){
                    percentChange = "$percentChange%"
                    binding.dailyChangeValue.text = percentChange
                    binding.dailyChangeValue.setTextColor(Color.GREEN)
                }
                else if (percentChange != null && percentChange.toDouble() < 0)
                {
                    percentChange = "$percentChange%"
                    binding.dailyChangeValue.text = percentChange
                    binding.dailyChangeValue.setTextColor(Color.RED)
                }
                else
                {
                    percentChange = "$percentChange%"
                    binding.dailyChangeValue.text = percentChange
                    binding.dailyChangeValue.setTextColor(Color.WHITE)
                }
            }
        }

        // observe basic financials and update text views on change
        viewModel.observeBasicFinancials().observe(this){
            val metricData = viewModel.observeBasicFinancials().value
            if (metricData != null) {
                binding.yearlyHighValue.text = "$ " + metricData.yearlyHigh
                binding.yearlyLowValue.text = "$ " + metricData.yearlyLow
                var netProfitMarginTTM = metricData.netProfitMarginTTM
                if (netProfitMarginTTM != null){
                    if (netProfitMarginTTM.toDouble() < 0){
                        binding.netProfitMarginTTMValue.text = netProfitMarginTTM + "%"
                        binding.netProfitMarginTTMValue.setTextColor(Color.RED)
                    }
                    else{
                        binding.netProfitMarginTTMValue.text = netProfitMarginTTM + "%"
                        binding.netProfitMarginTTMValue.setTextColor(Color.GREEN)
                    }
                }

            }
            println("metric data: " + metricData)
        }

        // observe insider sentiment results and update rating on change
        viewModel.observeFinnhubInsiderSentimentRestults().observe(this){
            // grab ust the last sentiment from the list
            val insiderSentiments = viewModel.observeFinnhubInsiderSentimentRestults().value
            if (insiderSentiments != null) {
                if (insiderSentiments.size > 0) {
                    val recentSentiment = insiderSentiments.get(insiderSentiments.size-1)
                    // determine buy rating using mspr
                    val mspr = recentSentiment.mspr.toDouble()
                    println("\n\nMSPR VALUE: " + mspr)
                    if (mspr > 75){
                        binding.insiderSentimentRating.text = "Strong Buy"
                        binding.insiderSentimentRating.setTextColor(Color.GREEN)
                    } else if(mspr > 25){
                        binding.insiderSentimentRating.text = "Buy"
                        binding.insiderSentimentRating.setTextColor(Color.GREEN)
                    } else if(mspr < -25 && mspr > -75){
                        binding.insiderSentimentRating.text = "Sell"
                        binding.insiderSentimentRating.setTextColor(Color.RED)
                    } else if(mspr < -75){
                        binding.insiderSentimentRating.text = "Strong Sell"
                        binding.insiderSentimentRating.setTextColor(Color.RED)
                    } else{
                        binding.insiderSentimentRating.text = "Hold"
                        binding.insiderSentimentRating.setTextColor(Color.BLUE)
                    }
                    println("mspr: " + recentSentiment.mspr)
                }
            }
        }

        // hit quote API endpoints based on symbol
        if (symbol != null) {
            viewModel.finnhubQuoteRequest(symbol)

            // get current date for insider sentiment request
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = current.format(formatter)
            viewModel.finnhubInsiderSentimentRequest(symbol, formatted)
            viewModel.finnhubBasicFinancialsRequest(symbol)
        }

        val fragmentManager = (this as AppCompatActivity).supportFragmentManager
        binding.addButton.setOnClickListener(){
            val addFolioPopUp = AddStockFragment()

            val args = Bundle()
            args.putString("symbol", symbol)
            args.putString("stockName", stockName)
            args.putString("currentPrice",currentPrice)
            addFolioPopUp.arguments = args

            addFolioPopUp.show(fragmentManager,"Purchase Stock")
        }

        binding.favoriteButton.setOnClickListener(){
            var fav = FavoriteData(
                uid = user,
                stock_ticker = symbol,
                stock_name = stockName,
            )

            if(binding.favoriteButton.text == "FAVORITE")
            {
                binding.favoriteButton.text = "UNFAVORITE"
            }
            else if(binding.favoriteButton.text == "UNFAVORITE")
            {
                binding.favoriteButton.text = "FAVORITE"
            }

            viewModel.updateFavorites(fav)
            //TODO figure out how to update this live
            /*favoritesList = viewModel.observeFavoriteDataList().value
            favoritesList?.let { it1 -> checkFavList(stockName, it1) }*/

        }
    }

    // end activity - can add more logic here if needed
    private fun doFinish(){
        viewModel.fetchFavoriteDataList(FirebaseAuth.getInstance().currentUser!!.uid)
        finish()
    }

    private fun checkFavList(stockName: String, favoritesList: MutableList<FavoriteData>){
        for(fav in favoritesList!!){
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
        // Handle action argsbaBundler item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == android.R.id.home) {
            // If user clicks "up", then it it as if they clicked OK.  We commit
            // changes and return to parent
            doFinish()
            true
        } else super.onOptionsItemSelected(item)
    }
}

