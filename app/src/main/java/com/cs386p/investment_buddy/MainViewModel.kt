package com.cs386p.investment_buddy

import androidx.lifecycle.MutableLiveData
import com.cs386p.investment_buddy.api.AlphaVantageAPI
import com.cs386p.investment_buddy.api.Repository
import com.cs386p.investment_buddy.api.SearchedStock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs386p.investment_buddy.api.Quote
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val alphaVantagAPI = AlphaVantageAPI.create()
    private val stockRepository = Repository(alphaVantagAPI)
    private val searchResults = MutableLiveData<MutableList<SearchedStock>?>()
    private val quoteResults = MutableLiveData<Quote>()

    fun symbolSearch(symbol: String) = viewModelScope.launch {
        val results = stockRepository.symbolSearch(symbol)
        val NYSEresults = mutableListOf<SearchedStock>()
        // manually filter search results to only include US stocks
        for (result in results){
            if (result.region == "United States"){
                NYSEresults.add(result)
            }
        }
        searchResults.postValue(NYSEresults)
    }

    fun observeSearchResults(): MutableLiveData<MutableList<SearchedStock>?> {
        return searchResults
    }

    fun quoteRequest(symbol: String) = viewModelScope.launch {
        val result = stockRepository.quoteRequest(symbol)
        println("\n\n********result for quote: " + result)
        quoteResults.postValue(result)
    }

    fun observeQuoteResults(): MutableLiveData<Quote>{
        return quoteResults
    }


}