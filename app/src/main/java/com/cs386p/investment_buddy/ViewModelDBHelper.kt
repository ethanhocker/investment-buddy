package com.cs386p.investment_buddy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.collections.TransactionsData
import com.cs386p.investment_buddy.collections.UsersData
import com.firebase.ui.auth.data.model.User
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.toObject
import kotlin.reflect.typeOf

class ViewModelDBHelper() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionHoldings = "Holdings"
    private val collectionTransactions = "Transactions"
    private val collectionUsers = "Users"




    fun dbFetchHoldings(holdingsList: MutableLiveData<List<HoldingsData>>){
        db.collection(collectionHoldings).orderBy("uuid", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "XXX Holdings fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                val temp = result.documents.mapNotNull {
                    it.toObject(HoldingsData::class.java)
                }
                Log.d("XXX", temp.javaClass.name)
                Log.d("XXX", "temp 0 uuid: " + temp[0].uuid)
                holdingsList.postValue(temp)

//                holdingsList.postValue(result.documents.mapNotNull {
//                    it.toObject(HoldingsMeta::class.java)
//                })
//                Log.d("XXX Result of Query: ", result.documents.toString())
//                Log.d("XXX", "\n\n************** stock ticker from view model" + (holdingsList.getValue()
//                    ?.get(0)?.stock_ticker
//                    ?: null))
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "XXX Holdings fetch FAILED ", it)
            }

//        Log.d("XXX Result of Query in holdingsList: ", holdingsList.value!![0].stock_ticker)
    }

    fun dbUpdateHoldings(holding: HoldingsData){

        db.collection(collectionHoldings).whereEqualTo("uuid",holding.uuid).whereEqualTo("stock_ticker",holding.stock_ticker).whereEqualTo("port_num",holding.port_num).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "XXX Holdings Update fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                if(result!!.documents.size > 0)
                {
                    db.collection(collectionHoldings).document(result.documents[0].id).set(holding)
                }
                else
                {
                    holding.firestoreID = db.collection(collectionHoldings).document().id
                    db.collection(collectionHoldings).document(holding.firestoreID).set(holding)
                }

            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "XXX Holdings Update fetch FAILED ", it)
            }


    }

    fun dbFetchTransactions(){

    }

    fun createTransaction(action: TransactionsData){
        Log.d("XXX Creating Transaction: ","HELPER")
        action.firestoreID = db.collection(collectionTransactions).document().id
        db.collection(collectionTransactions).document(action.firestoreID).set(action)

        Log.d("XXX DONE Creating Transaction: ","HELPER")
    }


    fun dbFetchUsers(){

    }

    fun updateUsersPortfolios(user: UsersData){
        db.collection(collectionUsers).whereEqualTo("uuid",user.uuid).whereEqualTo("port_num",user.port_num).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "XXX User Portfolios Update fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                if(result!!.documents.size > 0)
                {
                    db.collection(collectionUsers).document(result.documents[0].id).set(user)
                }
                else
                {
                    user.firestoreID = db.collection(collectionUsers).document().id
                    db.collection(collectionUsers).document(user.firestoreID).set(user)
                }

            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "XXX User Portfolios Update fetch FAILED ", it)
            }
    }


}