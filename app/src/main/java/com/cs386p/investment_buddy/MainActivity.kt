package com.cs386p.investment_buddy

//TODO Remove unused imports after moving unused functions

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs386p.investment_buddy.api.FinnhubQuote
import com.cs386p.investment_buddy.collections.FavoriteData
import com.cs386p.investment_buddy.databinding.ActivityMainBinding
import com.cs386p.investment_buddy.databinding.ContentMainBinding
import com.cs386p.investment_buddy.ui.FavoritesAdapter
import com.cs386p.investment_buddy.ui.Folios
import com.cs386p.investment_buddy.ui.StockSearch
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding : ContentMainBinding
    private lateinit var UID: String

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            viewModel.updateUser()
        }

    private val localFavoriteDataList = mutableListOf<FavoriteData>()
    val adapter = FavoritesAdapter()

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        binding = activityMainBinding.contentMain

        // log user in
        AuthInit(viewModel, signInLauncher)

        val layoutManager = LinearLayoutManager(this)
        binding.favoritesRecyclerView.layoutManager = layoutManager
        binding.favoritesRecyclerView.adapter = adapter

        // Set default mode for app to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        UID = viewModel.observeUID().value.toString()

        // start StockSearch activity when analyze text view is clicked
        binding.analyze.setOnClickListener {
            val stockSearchClass = StockSearch()
            val intent = Intent(this, stockSearchClass::class.java)
            this.startActivity(intent)
        }

        // start Folios activity when folios text view is clicked
        binding.fakefolios.setOnClickListener {
            val foliosClass = Folios()
            val intent = Intent(this, foliosClass::class.java)
            this.startActivity(intent)
        }

        // sign the user out of firestore when the sign out button is clicked
        binding.signOutBTN.setOnClickListener() {
            viewModel.signOut()
            AuthInit(viewModel, signInLauncher)
        }

        MainViewModel.fetchFavoritesData(UID)

        // A long with the accompanying layout, all instance of this code and similar code are largely derived from:
        // https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android

        // add popup for analyzing stocks info
        binding.analyzeInfo.setOnClickListener {
            // inflate the layout of the popup window
            val popupView = LayoutInflater.from(this).inflate(
                com.cs386p.investment_buddy.R.layout.popup_window,
                activityMainBinding.root,
                false
            )
            val infoTextView =
                popupView.findViewById<TextView>(com.cs386p.investment_buddy.R.id.infoText)
            infoTextView.text =
                "Analyze stocks allows you to search for NYSE stocks and view research about each stock."

            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // show the popup window
            popupWindow.showAtLocation(binding.analyzeInfo, Gravity.CENTER, 0, 0)

            // dismiss the popup window when touched
            popupView.setOnTouchListener { v, event ->
                popupWindow.dismiss()
                true
            }
        }

        // add popup for fakefolios info
        binding.fakefoliosInfo.setOnClickListener {
            // inflate the layout of the popup window
            val popupView = LayoutInflater.from(this).inflate(
                com.cs386p.investment_buddy.R.layout.popup_window,
                activityMainBinding.root,
                false
            )
            val infoTextView =
                popupView.findViewById<TextView>(com.cs386p.investment_buddy.R.id.infoText)
            infoTextView.text = "FakeFolios are fake stock portfolios you can manage risk-free. " +
                    "Stocks can be purchased for a FakeFolio from a Stock Research page."

            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // show the popup window
            popupWindow.showAtLocation(binding.fakefoliosInfo, Gravity.CENTER, 0, 0)

            // dismiss the popup window when touched
            popupView.setOnTouchListener { v, event ->
                popupWindow.dismiss()
                true
            }
        }

        // fetch favorites data from database and update adapter when favorites are changed
        if (UID != "Uninitialized") {
            viewModel.fetchFavoriteDataList(UID)
        }
        viewModel.observeFavoriteDataList().observe(this) {
            val results = viewModel.observeFavoriteDataList().value
            if (results != null) {
                for (result in results) {
                    viewModel.finnhubQuoteRequestFavorite(result.stock_ticker, this)
                }
            }
        }
    }

    // local list of favoriteData must be used to avoid race condition issues with posting to viewModel
    fun addFavoriteQuote(quote: FinnhubQuote){
        // grab list of favorites from view model
        val favoriteDataList = viewModel.observeFavoriteDataList().value
        if (favoriteDataList != null) {
            // check if each favorite's symbol is equal tot eh current quote
            for (favorite in favoriteDataList){
                if (quote != null && quote.percentChange != null) {
                    if (quote.symbol == favorite.stock_ticker){
                        // add cost and change values to favorite and add favorite to local list
                        favorite.dailyChange = quote.percentChange
                        favorite.cost = quote.currentPrice
                        localFavoriteDataList.add(favorite)
                    }
                }
            }
            // submit local favoriteDataList to adapter and notify of change
            adapter.submitList(localFavoriteDataList)
            adapter.notifyDataSetChanged()
        }
    }
}