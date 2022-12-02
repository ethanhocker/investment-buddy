package com.cs386p.investment_buddy.api

import com.google.gson.annotations.SerializedName

data class MetricData (
    @SerializedName("52WeekHigh")
    val yearlyHigh: String,
    @SerializedName("52WeekLow")
    val yearlyLow: String,
    val netProfitMarginTTM: String
)