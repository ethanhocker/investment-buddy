package com.cs386p.investment_buddy

import com.cs386p.investment_buddy.api.AlphaVantageAPI
import com.cs386p.investment_buddy.api.Repository
import com.cs386p.investment_buddy.api.SearchedStock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val alphaVantagAPI = AlphaVantageAPI.create()
    private val stockRepository = Repository(alphaVantagAPI)
    private val searchResults = mutableListOf<SearchedStock>()

    fun symbolSearch() = viewModelScope.launch {
        // TODO: rewrite this. Just testing the API here
        val test = stockRepository.symbolSearch("AAPL")
        println("test: " + test)
    }
}