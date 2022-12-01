package com.cs386p.investment_buddy

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth

//TODO Remove unused imports after moving unused functions
import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.collections.TransactionsData
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.collections.FavoritesData
import com.google.firebase.Timestamp

import com.cs386p.investment_buddy.databinding.ActivityMainBinding
import com.cs386p.investment_buddy.databinding.ContentMainBinding
import com.cs386p.investment_buddy.ui.*
import okhttp3.internal.notify


class MainActivity : AppCompatActivity() {
    val api_key = "RUXI1LX1OCUM137J" // TODO: Move this, just noting this here for now
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding : ContentMainBinding
    private lateinit var UID: String

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            viewModel.updateUser()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        binding = activityMainBinding.contentMain

        val layoutManager = LinearLayoutManager(this)
        binding.favoritesRecyclerView.layoutManager = layoutManager
        val adapter = FavoritesAdapter()
        binding.favoritesRecyclerView.adapter = adapter

        // Set default mode for app to dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        UID = viewModel.observeUUID().value.toString()
        println("\n\n****************** MainActivity UID: " + UID)

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

        binding.signOutBTN.setOnClickListener(){
            viewModel.signOut()
            AuthInit(viewModel, signInLauncher)
        }

        //viewModel.symbolSearch() //TODO: Delete me - just for testing

        AuthInit(viewModel, signInLauncher)

        MainViewModel.fetchFavoritesData(UID)

        viewModel.observeFavoritesData().observe(this){

            val results = viewModel.observeFavoritesData().value
            adapter.submitList(results)
        }

        //TODO: Delete the rest of these functions and inputs once they are placed elsewhere
        /*viewModel.fetchHoldingsData(UID)

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

        viewModel.updateFavorites(fav)*/

        /*viewModel.fetchFoliosData(FirebaseAuth.getInstance().currentUser!!.uid)

        viewModel.observeFoliosData().observe(this){
            //println("\n\n****************** Favorites STOCK TICKER: " + it[0].stock_ticker)
            //findViewById<TextView>(R.id.hello).text = it[0].stock_ticker

            val rv = findViewById<RecyclerView>(R.id.foliosRecyclerView)
            rv.layoutManager = LinearLayoutManager(this)

            val adapter = FoliosAdapter(it)
            rv.adapter = adapter

        }*/


    }
}