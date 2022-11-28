package com.cs386p.investment_buddy.api

class Repository(private val api: AlphaVantageAPI) {
    suspend fun symbolSearch(symbol: String): List<SearchedStock> {
        return api.getSymbolSearch(symbol).bestMatches
    }
    suspend fun quoteRequest(symbol: String): Quote {
        return api.getQuote(symbol).globalQuote
    }
}