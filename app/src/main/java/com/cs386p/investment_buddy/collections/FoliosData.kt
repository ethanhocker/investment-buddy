package com.cs386p.investment_buddy.collections

import com.google.firebase.firestore.DocumentId

class FoliosData  (
    var uuid: String = "",
    var port_num: Long = 0L,
    var aab: Double = 0.0,
    @DocumentId var firestoreID: String = ""
)