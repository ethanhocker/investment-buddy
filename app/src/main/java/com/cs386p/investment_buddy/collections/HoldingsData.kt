package com.cs386p.investment_buddy.collections

import com.google.firebase.firestore.DocumentId

data class HoldingsData(
    var uid: String = "",
    var units: Double = 0.0,
    var stock_ticker: String = "",
    var stock_name: String = "",
    var inception_date: String = "",
    var port_num: String = "",
    @DocumentId var firestoreID: String = ""
)