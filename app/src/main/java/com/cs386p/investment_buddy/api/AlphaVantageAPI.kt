package com.cs386p.investment_buddy.api

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AlphaVantageAPI {
    @GET("query?function=SYMBOL_SEARCH&apikey=RUXI1LX1OCUM137J")
    suspend fun getSymbolSearch(@Query("keywords") symbol: String) : SearchedStockResponse

    data class SearchedStockResponse(val bestMatches: List<SearchedStock>){}

    companion object {
        // Leave this as a simple, base URL.  That way, we can have many different
        // functions (above) that access different "paths" on this server
        // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-http-url/
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("alphavantage.co")
            .build()

        // Public create function that ties together building the base
        // URL and the private create function that initializes Retrofit
        fun create(): AlphaVantageAPI = create(url)
        private fun create(httpUrl: HttpUrl): AlphaVantageAPI {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AlphaVantageAPI::class.java)
        }
    }
}