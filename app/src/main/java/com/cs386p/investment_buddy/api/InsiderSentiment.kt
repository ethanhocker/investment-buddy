package com.cs386p.investment_buddy.api

data class InsiderSentiment (
    val symbol: String,
    val year: String,
    val month: String,
    val change: String,
    val mspr: String
)