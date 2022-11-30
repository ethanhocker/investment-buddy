package com.cs386p.investment_buddy.api

import com.google.gson.annotations.SerializedName

data class FinnhubQuote (
    @SerializedName("c")
    val currentPrice: String,
    @SerializedName("d")
    val change: String,
    @SerializedName("dp")
    val percentChange: String,
    @SerializedName("h")
    val dailyHigh: String,
    @SerializedName("l")
    val dailyLow: String,
    @SerializedName("o")
    val open: String,
    @SerializedName("pc")
    val previousClose: String
)