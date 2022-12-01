package com.cs386p.investment_buddy.collections

import com.google.firebase.firestore.DocumentId

class FoliosData(
    var uid: String = "",
    var port_num: Long = 0L,
    var aab: Double = 0.0,
    var name: String = "",
    @DocumentId var firestoreID: String = ""
)