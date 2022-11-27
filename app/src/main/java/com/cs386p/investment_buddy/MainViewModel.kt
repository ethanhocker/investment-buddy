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
import com.cs386p.investment_buddy.collections.UsersData
import kotlinx.coroutines.launch

import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel() {
    private val alphaVantagAPI = AlphaVantageAPI.create()
    private val stockRepository = Repository(alphaVantagAPI)
    private val searchResults = mutableListOf<SearchedStock>()

    var holdingsDataList = MutableLiveData<List<HoldingsData>>()

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

        //Log.d("XXX Result of Query: ", holdingsMetaList.value!![0].ticker)
//        println("\n\n********* holdings meta list size: " + (holdingsMetaList.value?.size ?: null))
//        println("\n\n********* ticker value: " + holdingsMetaList.value?.get(0)?.stock_ticker)
    }
    fun observeHoldingsData(): LiveData<List<HoldingsData>> {
        return holdingsDataList
    }

    fun updateHolding(holding: HoldingsData) {
        dbHelp.dbUpdateHoldings(holding)
    }

    fun createTransaction(action: TransactionsData){
        Log.d("XXX Creating Transaction: ","MVM")
        dbHelp.createTransaction(action)
    }

    fun updateUsersPorts(user: UsersData) {
        Log.d("XXX Updating User Portfolios: ","MVM")
        dbHelp.updateUsersPortfolios(user)
    }
}