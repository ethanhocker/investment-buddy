package com.cs386p.investment_buddy.collections

import com.google.firebase.firestore.DocumentId

class FavoritesData (
    var uuid: String = "",
    var stock_ticker: String = "",
    var port_num: Long = 0L,
    @DocumentId var firestoreID: String = ""
)