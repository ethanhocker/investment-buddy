package com.cs386p.investment_buddy.collections

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

class TransactionsData (
    var uid: String = "",
    var stock_ticker: String = "",
    @ServerTimestamp val timeStamp: Timestamp? = null,
    var unit_price: Double = 0.0,
    var units_purchased: Double = 0.0,
    var port_num: Long = 0L,
    @DocumentId var firestoreID: String = ""
)