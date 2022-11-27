package com.cs386p.investment_buddy

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.widget.TextView

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth


import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.collections.TransactionsData
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.collections.FavoritesData
import com.google.firebase.Timestamp


class MainActivity : AppCompatActivity() {
    val api_key = "RUXI1LX1OCUM137J" // TODO: Move this, just noting this here for now
    private val viewModel: MainViewModel by viewModels()

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            viewModel.updateUser()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set default mode for app to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        viewModel.symbolSearch() //TODO: Delete me - just for testing

        AuthInit(viewModel, signInLauncher)
        viewModel.fetchHoldingsData()

        viewModel.observeHoldingsData().observe(this){
            println("\n\n****************** STOCK TICKER: " + it[0].stock_ticker)
            //findViewById<TextView>(R.id.hello).text = it[0].stock_ticker
        }

        var holding = HoldingsData(
            uuid = FirebaseAuth.getInstance().currentUser!!.uid,
            units = 69.0,
            stock_ticker = "CLF",
            port_num = 3
        )

        viewModel.updateHolding(holding)

        var action = TransactionsData(
            uuid = FirebaseAuth.getInstance().currentUser!!.uid,
            stock_ticker = "CLF",
            timeStamp =  Timestamp.now(),
            unit_price = 70.25,
            units_purchased = 69.0,
            port_num = 2
        )

        viewModel.createTransaction(action)

        var folio = FoliosData(
            uuid = FirebaseAuth.getInstance().currentUser!!.uid,
            port_num = 1,
            aab = 3000.0
        )

        viewModel.updateFolios(folio)

        var fav = FavoritesData(
            uuid = FirebaseAuth.getInstance().currentUser!!.uid,
            port_num = 1,
            stock_ticker = "AMD"
        )

        viewModel.updateFavorites(fav)


//        findViewById<TextView>(R.id.hello).text = viewModel.holdingsMetaList.value!![0].stock_ticker

        //Log.d("Result of Query: ", viewModel.holdingsMetaList.value!![0].ticker)

    }
}