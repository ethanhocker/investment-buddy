package com.cs386p.investment_buddy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs386p.investment_buddy.api.*
import com.cs386p.investment_buddy.collections.*

import kotlinx.coroutines.launch

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineStart

class MainViewModel : ViewModel() {
    private val alphaVantagAPI = AlphaVantageAPI.create()
    private val finnhubAPI = FinnhubAPI.create()
    private val stockRepository = Repository(alphaVantagAPI, finnhubAPI)
    private val searchResults = MutableLiveData<MutableList<SearchedStock>?>()
    private val alphaQuoteResults = MutableLiveData<AlphaQuote>()
    private val finnhubQuoteResults = MutableLiveData<FinnhubQuote>()
    private val favoriteQuote = MutableLiveData<FinnhubQuote>()
    private val finnhubInsiderSentimentResults = MutableLiveData<MutableList<InsiderSentiment>>()
    private val finnhubBasicFinancialsResults = MutableLiveData<MetricData>()

    private var UID = MutableLiveData("Uninitialized")

    var holdingsDataList = MutableLiveData<MutableList<HoldingsData>>()
    var favoriteDataList = MutableLiveData<MutableList<FavoriteData>>()

    private var folioName = MutableLiveData<String>()

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun updateUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null) {
            UID.postValue(FirebaseAuth.getInstance().currentUser!!.uid)
        }
    }

    fun observeUID() : LiveData<String>{
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

    fun fetchHoldingsData(uid: String, portNum: String) {
        dbHelp.dbFetchHoldings(uid,portNum, holdingsDataList)
    }

    fun observeHoldingsData(): MutableLiveData<MutableList<HoldingsData>> {
        return holdingsDataList
    }

    fun updateHolding(holding: HoldingsData) {
        dbHelp.dbUpdateHoldings(holding)
    }

    fun createTransaction(action: TransactionsData){
        Log.d("Creating Transaction: ","MVM")
        dbHelp.dbCreateTransaction(action)
    }

    fun observeFoliosData(): MutableLiveData<MutableList<FoliosData>> {
        return foliosDataList
    }

    fun updateFolio(folio: FoliosData) {
        Log.d("Updating User Folios: ","MVM")
        dbHelp.dbUpdateFolio(folio)
    }

    fun updateFolioName(name: String){
        folioName.postValue(name)
    }

    fun observeFolioName(): String{
        return folioName.value.toString()
    }

    fun fetchFavoriteDataList(uid: String){
        dbHelp.dbFetchFavorites(uid, favoriteDataList)
    }

    fun observeFavoriteDataList(): MutableLiveData<MutableList<FavoriteData>> {
        return favoriteDataList
    }

    fun updateFavorites(fav: FavoriteData) {
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

    fun finnhubQuoteRequestFavorite(symbol: String, mainActivity: MainActivity) {
        println("launched request for single stock quote")
        viewModelScope.launch (start = CoroutineStart.ATOMIC) {
            val result = stockRepository.finnhubQuoteRequest(symbol)
            favoriteQuote.postValue(result)
            mainActivity.addFavoriteQuote(result)
        }
    }

    fun observeQuoteRequestFavorite(): MutableLiveData<FinnhubQuote>{
        return favoriteQuote
    }

    fun finnhubQuoteRequest(symbol: String) = viewModelScope.launch {
        val result = stockRepository.finnhubQuoteRequest(symbol)
        finnhubQuoteResults.postValue(result)
    }

    fun observeFinnhubQuoteResults(): MutableLiveData<FinnhubQuote>{
        return finnhubQuoteResults
    }

    fun finnhubInsiderSentimentRequest(symbol: String, date: String) = viewModelScope.launch {
        val result = stockRepository.finnhubInsiderSentimentRequest(symbol, date)
        println("result from repository: " + result)
        println("result from repository size: " + result.size)
        finnhubInsiderSentimentResults.postValue(result as MutableList<InsiderSentiment>?)
    }

    fun observeFinnhubInsiderSentimentRestults(): MutableLiveData<MutableList<InsiderSentiment>>{
        return finnhubInsiderSentimentResults
    }

    fun finnhubBasicFinancialsRequest(symbol: String) = viewModelScope.launch {
        val result = stockRepository.finnhubBasicFinancialsRequest(symbol)
        finnhubBasicFinancialsResults.postValue(result)
    }

    fun observeBasicFinancials(): MutableLiveData<MetricData>{
        return finnhubBasicFinancialsResults
    }

    companion object{
        var foliosDataList = MutableLiveData<MutableList<FoliosData>>()
        var favoritesDataList = MutableLiveData<MutableList<FavoriteData>>()
        private val dbHelp = ViewModelDBHelper()

        fun deleteFolios(uid: String, folioName: String){
            dbHelp.dbDeleteFolios(uid,folioName)
        }
        fun fetchFoliosData(uid: String){
            dbHelp.dbFetchFolios(uid,foliosDataList)
        }

        fun fetchFavoritesData(uid: String){
            dbHelp.dbFetchFavorites(uid,favoritesDataList)
        }

    }
}