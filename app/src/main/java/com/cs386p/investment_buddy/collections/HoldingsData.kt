package com.cs386p.investment_buddy.collections


import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class HoldingsData(
    var uuid: String = "",
    var units: Double = 0.0,
    var stock_ticker: String = "",
    var port_num: Long = 0L,
    @DocumentId var firestoreID: String = ""
)