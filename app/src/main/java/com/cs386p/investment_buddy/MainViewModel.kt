package com.cs386p.investment_buddy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs386p.investment_buddy.api.*

import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.collections.TransactionsData
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.collections.FavoritesData

import kotlinx.coroutines.launch

import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel() {
    private val alphaVantagAPI = AlphaVantageAPI.create()
    private val finnhubAPI = FinnhubAPI.create()
    private val stockRepository = Repository(alphaVantagAPI, finnhubAPI)
    private val searchResults = MutableLiveData<MutableList<SearchedStock>?>()
    private val alphaQuoteResults = MutableLiveData<AlphaQuote>()
    private val finnhubQuoteResults = MutableLiveData<FinnhubQuote>()

    private var UID = MutableLiveData("Uninitialized")

    var holdingsDataList = MutableLiveData<List<HoldingsData>>()
    var favoritesDataList = MutableLiveData<List<FavoritesData>>()
    var foliosDataList = MutableLiveData<List<FoliosData>>()

    private val dbHelp = ViewModelDBHelper()

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun updateUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null) {
            UID.postValue(FirebaseAuth.getInstance().currentUser!!.uid)
        }
    }

    fun observeUUID() : LiveData<String>{
        return UID
    }

    fun symbolSearch(symbol: String) = viewModelScope.launch {
        val results = stockRepository.symbolSearch(symbol)
        val NYSEresults = mutableListOf<SearchedStock>()
        // manually filter search results to only include US stocks
        if (results != null) {
            for (result in results) {
                if (result.region == "United States") {
                    NYSEresults.add(result)
                }
            }
        }
        searchResults.postValue(NYSEresults)
    }

    fun observeSearchResults(): MutableLiveData<MutableList<SearchedStock>?> {
        return searchResults
    }

    /*fun updateDashboard() {
    }*/

    fun fetchHoldingsData(uid: String) {
        dbHelp.dbFetchHoldings(uid, holdingsDataList)
    }

    fun observeHoldingsData(): LiveData<List<HoldingsData>> {
        return holdingsDataList
    }

    fun updateHolding(holding: HoldingsData) {
        dbHelp.dbUpdateHoldings(holding)
    }

    fun createTransaction(action: TransactionsData){
        Log.d("Creating Transaction: ","MVM")
        dbHelp.dbCreateTransaction(action)
    }

    fun fetchFoliosData(uid: String){
        dbHelp.dbFetchFolios(uid,foliosDataList)
    }

    fun observeFoliosData(): LiveData<List<FoliosData>> {
        return foliosDataList
    }

    fun updateFolios(folio: FoliosData) {
        Log.d("Updating User Folios: ","MVM")
        dbHelp.dbUpdateFolios(folio)
    }

    fun fetchFavoritesData(uuid: String){
        dbHelp.dbFetchFavorites(uuid,favoritesDataList)
    }

    fun observeFavoritesData(): LiveData<List<FavoritesData>> {
        return favoritesDataList
    }

    fun updateFavorites(fav: FavoritesData) {
        Log.d("Updating User Folios: ","MVM")
        dbHelp.dbUpdateFavorites(fav)
    }

    fun alphaQuoteRequest(symbol: String) = viewModelScope.launch {
        val result = stockRepository.alphaQuoteRequest(symbol)
        alphaQuoteResults.postValue(result)
    }

    fun observeAlphaQuoteResults(): MutableLiveData<AlphaQuote>{
        return alphaQuoteResults
    }

    fun finnhubQuoteRequest(symbol: String) = viewModelScope.launch {
        val result = stockRepository.finnhubQuoteRequest(symbol)
        finnhubQuoteResults.postValue(result)
    }

    fun observeFinnhubQuoteResults(): MutableLiveData<FinnhubQuote>{
        return finnhubQuoteResults
    }
}