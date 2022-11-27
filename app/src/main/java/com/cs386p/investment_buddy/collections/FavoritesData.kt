package com.cs386p.investment_buddy.collections

import com.google.firebase.firestore.DocumentId

class FavoritesData (
    var uuid: String = "",
    var stock_ticker: String = "",
    var stock_name: String = "",
    @DocumentId var firestoreID: String = ""
)