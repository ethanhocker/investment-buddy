package com.cs386p.investment_buddy.api

class Repository(private val alphaApi: AlphaVantageAPI, private val finnhubApi: FinnhubAPI) {
    suspend fun symbolSearch(symbol: String): List<SearchedStock> {
        return alphaApi.getSymbolSearch(symbol).bestMatches
    }
    suspend fun alphaQuoteRequest(symbol: String): AlphaQuote {
        return alphaApi.getQuote(symbol).globalQuote
    }
    // finnhub quotes have a less strict request limit of 60 api calls/minute
    suspend fun finnhubQuoteRequest(symbol: String): FinnhubQuote {
        var result = finnhubApi.getQuote(symbol)
        result.symbol = symbol
        return result
    }
}