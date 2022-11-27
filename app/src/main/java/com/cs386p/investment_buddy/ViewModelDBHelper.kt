package com.cs386p.investment_buddy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.collections.TransactionsData
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.collections.FavoritesData
import com.firebase.ui.auth.data.model.User
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.toObject
import kotlin.reflect.typeOf

class ViewModelDBHelper() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionHoldings = "Holdings"
    private val collectionTransactions = "Transactions"
    private val collectionFolios = "Folios"
    private val collectionFavorites = "Favorites"




    fun dbFetchHoldings(holdingsList: MutableLiveData<List<HoldingsData>>){
        db.collection(collectionHoldings).orderBy("uuid", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "XXX Holdings fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                val temp = result.documents.mapNotNull {
                    it.toObject(HoldingsData::class.java)
                }
                holdingsList.postValue(temp)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "XXX Holdings fetch FAILED ", it)
            }
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

    fun dbCreateTransaction(action: TransactionsData){
        Log.d("XXX Creating Transaction: ","HELPER")
        action.firestoreID = db.collection(collectionTransactions).document().id
        db.collection(collectionTransactions).document(action.firestoreID).set(action)

        Log.d("XXX DONE Creating Transaction: ","HELPER")
    }


    fun dbFetchFolios(user: String, foliosList: MutableLiveData<List<FoliosData>>){
        db.collection(collectionFolios).whereEqualTo("uuid",user).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "XXX Folios fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                val temp = result.documents.mapNotNull {
                    it.toObject(FoliosData::class.java)
                }
                foliosList.postValue(temp)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "XXX Folios fetch FAILED ", it)
            }
    }

    fun dbUpdateFolios(folio: FoliosData){
        db.collection(collectionFolios).whereEqualTo("uuid",folio.uuid).whereEqualTo("port_num",folio.port_num).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "XXX Portfolios Update fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                if(result!!.documents.size > 0)
                {
                    db.collection(collectionFolios).document(result.documents[0].id).set(folio)
                    Log.d("XXX Updating Existing Folio: ","HELPER")
                }
                else
                {
                    folio.firestoreID = db.collection(collectionFolios).document().id
                    db.collection(collectionFolios).document(folio.firestoreID).set(folio)
                    Log.d("XXX Creating New Folio: ","HELPER")
                }

            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "XXX Portfolios Update fetch FAILED ", it)
            }
    }


    fun dbFetchFavorites(user: String, favoritesList: MutableLiveData<List<FavoritesData>>){
        db.collection(collectionFavorites).whereEqualTo("uuid",user).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "XXX Favorites fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                val temp = result.documents.mapNotNull {
                    it.toObject(FavoritesData::class.java)
                }
                favoritesList.postValue(temp)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "XXX Favorites fetch FAILED ", it)
            }
    }

    fun dbUpdateFavorites(fav: FavoritesData){
        db.collection(collectionFavorites).whereEqualTo("uuid",fav.uuid).whereEqualTo("stock_ticker",fav.stock_ticker).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "XXX Favorites Update fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                if(result!!.documents.size > 0)
                {
                    db.collection(collectionFavorites).document(result.documents[0].id).delete()
                    Log.d("XXX Updating Existing Favorite: ","HELPER")
                }
                else
                {
                    fav.firestoreID = db.collection(collectionFavorites).document().id
                    db.collection(collectionFavorites).document(fav.firestoreID).set(fav)
                    Log.d("XXX Creating New Favorite: ","HELPER")
                }

            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "XXX Favorites Update fetch FAILED ", it)
            }
    }

}