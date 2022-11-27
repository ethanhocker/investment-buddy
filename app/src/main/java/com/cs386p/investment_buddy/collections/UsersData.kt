package com.cs386p.investment_buddy.collections

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

class UsersData (
    var uuid: String = "",
    var port_num: Long = 0L,
    var aab: Double = 0.0,
    @DocumentId var firestoreID: String = ""
)