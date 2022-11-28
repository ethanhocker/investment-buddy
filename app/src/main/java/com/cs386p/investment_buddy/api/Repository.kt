package com.cs386p.investment_buddy.api

class Repository(private val api: AlphaVantageAPI) {
    suspend fun symbolSearch(symbol: String): List<SearchedStock> {
        return api.getSymbolSearch(symbol).bestMatches
    }
    suspend fun quoteRequest(symbol: String): Quote {
        val result = api.getQuote(symbol).globalQuote
//        val result = api.getQuote(symbol)
        println("\n\n RESULT IN REPOSITORY: " + result)
        return result
//        return Quote("", "", "", "", "", "", "", "", "", "")
    }
}