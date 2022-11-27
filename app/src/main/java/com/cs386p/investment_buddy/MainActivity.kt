package com.cs386p.investment_buddy

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth


import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.collections.TransactionsData
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.collections.FavoritesData
import com.google.firebase.Timestamp

import com.cs386p.investment_buddy.databinding.ActivityMainBinding
import com.cs386p.investment_buddy.databinding.ContentMainBinding
import com.cs386p.investment_buddy.ui.FavoritesAdapter
import com.cs386p.investment_buddy.ui.StockSearch
import com.cs386p.investment_buddy.ui.Folios
import com.cs386p.investment_buddy.ui.FoliosAdapter
import okhttp3.internal.notify


class MainActivity : AppCompatActivity() {
    val api_key = "RUXI1LX1OCUM137J" // TODO: Move this, just noting this here for now
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding : ContentMainBinding

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            viewModel.updateUser()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        binding = activityMainBinding.contentMain

        // Set default mode for app to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        // Start StockSearch activity when analyze text view is clicked
        binding.analyze.setOnClickListener {
            val stockSearchClass = StockSearch()
            val intent = Intent(this, stockSearchClass::class.java)
            this.startActivity(intent)
        }

        binding.fakefolios.setOnClickListener{
            val foliosClass = Folios()
            val intent = Intent(this, foliosClass::class.java)
            this.startActivity(intent)
        }

        //viewModel.symbolSearch() //TODO: Delete me - just for testing

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

        //viewModel.createTransaction(action)

        var folio = FoliosData(
            uuid = FirebaseAuth.getInstance().currentUser!!.uid,
            port_num = 2,
            aab = 8000.0,
            name = "FakeFolio 00"
        )

        viewModel.updateFolios(folio)

        var fav = FavoritesData(
            uuid = FirebaseAuth.getInstance().currentUser!!.uid,
            stock_ticker = "AMD",
            stock_name = "Advanced Micro Devices"
        )

        viewModel.updateFavorites(fav)

        /*val adapter = FavoritesAdapter(listOf<FavoritesData>(fav))
        adapter.notifyDataSetChanged()*/

        viewModel.fetchFavoritesData(FirebaseAuth.getInstance().currentUser!!.uid)


        viewModel.observeFavoritesData().observe(this){
            println("\n\n****************** Favorites STOCK TICKER: " + it[0].stock_ticker)
            //findViewById<TextView>(R.id.hello).text = it[0].stock_ticker

            val rv = findViewById<RecyclerView>(R.id.favoritesRecyclerView)
            rv.layoutManager = LinearLayoutManager(this)

            val adapter = FavoritesAdapter(it)
            rv.adapter = adapter

        }




        viewModel.fetchFoliosData(FirebaseAuth.getInstance().currentUser!!.uid)

        /*viewModel.observeFoliosData().observe(this){
            //println("\n\n****************** Favorites STOCK TICKER: " + it[0].stock_ticker)
            //findViewById<TextView>(R.id.hello).text = it[0].stock_ticker

            val rv = findViewById<RecyclerView>(R.id.foliosRecyclerView)
            rv.layoutManager = LinearLayoutManager(this)

            val adapter = FoliosAdapter(it)
            rv.adapter = adapter

        }*/


//        findViewById<TextView>(R.id.hello).text = viewModel.holdingsMetaList.value!![0].stock_ticker

        //Log.d("Result of Query: ", viewModel.holdingsMetaList.value!![0].ticker)

    }
}