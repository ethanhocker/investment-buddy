package com.cs386p.investment_buddy

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cs386p.investment_buddy.api.AlphaVantageAPI
import com.cs386p.investment_buddy.api.Repository
import com.cs386p.investment_buddy.api.SearchedStock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.collections.TransactionsData
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.collections.FavoritesData
import kotlinx.coroutines.launch

import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel() {
    private val alphaVantagAPI = AlphaVantageAPI.create()
    private val stockRepository = Repository(alphaVantagAPI)
    private val searchResults = mutableListOf<SearchedStock>()

    var holdingsDataList = MutableLiveData<List<HoldingsData>>()
    var favoritesDataList = MutableLiveData<List<FavoritesData>>()
    var foliosDataList = MutableLiveData<List<FoliosData>>()

    private val dbHelp = ViewModelDBHelper()



    fun symbolSearch() = viewModelScope.launch {
        // TODO: rewrite this. Just testing the API here
        val test = stockRepository.symbolSearch("AAPL")
        println("test: " + test)
    }


    fun updateUser() {
        // XXX Write me. Update user data in view model
        val user = FirebaseAuth.getInstance().currentUser
    }
    fun updateDashboard() {
    }



    fun fetchHoldingsData() {
        dbHelp.dbFetchHoldings(holdingsDataList)
    }

    fun observeHoldingsData(): LiveData<List<HoldingsData>> {
        return holdingsDataList
    }

    fun updateHolding(holding: HoldingsData) {
        dbHelp.dbUpdateHoldings(holding)
    }



    fun createTransaction(action: TransactionsData){
        Log.d("XXX Creating Transaction: ","MVM")
        dbHelp.dbCreateTransaction(action)
    }


    fun fetchFoliosData(uuid: String){
        dbHelp.dbFetchFolios(uuid,foliosDataList)
    }

    fun observeFoliosData(): LiveData<List<FoliosData>> {
        return foliosDataList
    }

    fun updateFolios(folio: FoliosData) {
        Log.d("XXX Updating User Portfolios: ","MVM")
        dbHelp.dbUpdateFolios(folio)
    }


    fun fetchFavoritesData(uuid: String){
        dbHelp.dbFetchFavorites(uuid,favoritesDataList)
    }

    fun observeFavoritesData(): LiveData<List<FavoritesData>> {
        return favoritesDataList
    }

    fun updateFavorites(fav: FavoritesData) {
        Log.d("XXX Updating User Portfolios: ","MVM")
        dbHelp.dbUpdateFavorites(fav)
    }
}