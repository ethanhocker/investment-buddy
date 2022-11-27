package com.cs386p.investment_buddy

import androidx.lifecycle.MutableLiveData
import com.cs386p.investment_buddy.api.AlphaVantageAPI
import com.cs386p.investment_buddy.api.Repository
import com.cs386p.investment_buddy.api.SearchedStock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val alphaVantagAPI = AlphaVantageAPI.create()
    private val stockRepository = Repository(alphaVantagAPI)
    private val searchResults = MutableLiveData<MutableList<SearchedStock>?>()

    fun symbolSearch(symbol: String) = viewModelScope.launch {
        // TODO: rewrite this. Just testing the API here
        val results = stockRepository.symbolSearch(symbol)
        searchResults.postValue(results.toMutableList())
    }

    fun observeSearchResults(): MutableLiveData<MutableList<SearchedStock>?> {
        return searchResults
    }
}