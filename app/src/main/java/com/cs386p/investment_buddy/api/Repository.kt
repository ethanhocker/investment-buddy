package com.cs386p.investment_buddy.api

class Repository(private val api: AlphaVantageAPI) {
    suspend fun symbolSearch(symbol: String): List<SearchedStock> {
        val results = api.getSymbolSearch(symbol).bestMatches
        return results
    }
}