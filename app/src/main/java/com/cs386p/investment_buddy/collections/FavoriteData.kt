package com.cs386p.investment_buddy.collections

import com.google.firebase.firestore.DocumentId

class FavoriteData (
    var uid: String = "",
    var stock_ticker: String = "",
    var stock_name: String = "",
    var cost: String = "",
    var dailyChange: String = "",
    @DocumentId var firestoreID: String = ""
)