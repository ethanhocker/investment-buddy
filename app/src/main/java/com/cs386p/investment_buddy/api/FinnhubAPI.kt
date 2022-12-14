package com.cs386p.investment_buddy.api

import com.google.gson.annotations.SerializedName
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubAPI {
    @GET("api/v1/quote?token=ce23nnaad3idecbgor70ce23nnaad3idecbgor7g")
    suspend fun getQuote(@Query("symbol") symbol: String) : FinnhubQuote
    @GET("api/v1/stock/insider-sentiment?from=2022-01-01&token=ce23nnaad3idecbgor70ce23nnaad3idecbgor7g")
    suspend fun getInsiderSentiment(@Query("symbol") symbol: String, @Query("to") date: String ) : InsiderSentimentResponse
    @GET("api/v1/stock/metric?metric=all&token=ce23nnaad3idecbgor70ce23nnaad3idecbgor7g")
    suspend fun getBasicFinancials(@Query("symbol") symbol: String) : BasicFinancialsResponse

    data class InsiderSentimentResponse(val data: List<InsiderSentiment>){}
    data class BasicFinancialsResponse(val metric: MetricData){}

    companion object {
        // Leave this as a simple, base URL.  That way, we can have many different
        // functions (above) that access different "paths" on this server
        // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-http-url/
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("finnhub.io")
            .build()

        // Public create function that ties together building the base
        // URL and the private create function that initializes Retrofit
        fun create(): FinnhubAPI = create(url)
        private fun create(httpUrl: HttpUrl): FinnhubAPI {
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
                .create(FinnhubAPI::class.java)
        }
    }
}