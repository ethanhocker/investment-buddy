package com.cs386p.investment_buddy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

import com.cs386p.investment_buddy.collections.HoldingsData
import com.cs386p.investment_buddy.collections.TransactionsData
import com.cs386p.investment_buddy.collections.FoliosData
import com.cs386p.investment_buddy.collections.FavoriteData

class ViewModelDBHelper() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionHoldings = "Holdings"
    private val collectionTransactions = "Transactions"
    private val collectionFolios = "Folios"
    private val collectionFavorites = "Favorites"

    fun dbFetchHoldings(user: String, portNum: String, holdingsList: MutableLiveData<MutableList<HoldingsData>>){
        val Holdingsresults = mutableListOf<HoldingsData>()

        db.collection(collectionHoldings).whereEqualTo("uid",user).whereEqualTo("port_num", portNum).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Holdings fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                result.documents.mapNotNull {
                    Holdingsresults.add(it.toObject(HoldingsData::class.java)!!)
                }
                holdingsList.postValue(Holdingsresults)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "Holdings fetch FAILED ", it)
            }
    }

    fun dbUpdateHoldings(holding: HoldingsData){
        db.collection(collectionHoldings).whereEqualTo("uid",holding.uid).whereEqualTo("stock_ticker",holding.stock_ticker).whereEqualTo("port_num",holding.port_num).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Holdings Update fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                if(result!!.documents.size > 0)
                {
                    val currentUnits = result.documents[0].get("units").toString().toDouble()
                    val newUnits = holding.units

                    holding.inception_date = result.documents[0].get("inception_date").toString()
                    holding.units = currentUnits + newUnits

                    db.collection(collectionHoldings).document(result.documents[0].id).set(holding)
                }
                else
                {
                    holding.firestoreID = db.collection(collectionHoldings).document().id
                    db.collection(collectionHoldings).document(holding.firestoreID).set(holding)
                }

            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "Holdings Update fetch FAILED ", it)
            }
    }

    fun dbCreateTransaction(action: TransactionsData){
        action.firestoreID = db.collection(collectionTransactions).document().id
        db.collection(collectionTransactions).document(action.firestoreID).set(action)
    }

    fun dbFetchFolios(user: String, foliosList: MutableLiveData<MutableList<FoliosData>>){
        val Folioresults = mutableListOf<FoliosData>()

        db.collection(collectionFolios).whereEqualTo("uid",user).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Folios fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                result.documents.mapNotNull {
                    Folioresults.add(it.toObject(FoliosData::class.java)!!)
                }
                foliosList.postValue(Folioresults)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "Folios fetch FAILED ", it)
            }
    }

    fun dbUpdateFolio(folio: FoliosData){
        db.collection(collectionFolios).whereEqualTo("uid",folio.uid).whereEqualTo("port_num",folio.port_num).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Folios Update fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                if(result!!.documents.size > 0)
                {
                    val currentAAB = result.documents[0].get("aab").toString().toDouble()
                    val spentOnTransaction = folio.aab

                    folio.aab = currentAAB - spentOnTransaction
                    db.collection(collectionFolios).document(result.documents[0].id).set(folio)
                }
                else
                {
                    folio.firestoreID = db.collection(collectionFolios).document().id
                    db.collection(collectionFolios).document(folio.firestoreID).set(folio).addOnSuccessListener {
                        MainViewModel.fetchFoliosData(folio.uid)
                    }
                }
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "Folios Update fetch FAILED ", it)
            }
    }

    fun dbDeleteFolioAndHoldings(user: String, portNum: String){
        db.collection(collectionFolios).whereEqualTo("uid",user).whereEqualTo("port_num",portNum).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Folios Update fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                if(result!!.documents.size > 0)
                {
                    db.collection(collectionFolios).document(result.documents[0].id).delete().addOnSuccessListener {
                        MainViewModel.fetchFoliosData(user)
                    }
                }
                else
                {

                }
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "Folios Update fetch FAILED ", it)
            }

        db.collection(collectionHoldings).whereEqualTo("uid",user).whereEqualTo("port_num",portNum).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Folios Update fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                if(result!!.documents.size > 0)
                {
                    for(i in 0 until result!!.documents.size)
                    {
                        db.collection(collectionHoldings).document(result.documents[i].id).delete().addOnSuccessListener {
                            MainViewModel.fetchHoldingsData(user,portNum)
                        }
                    }
                }
                else
                {

                }
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "Folios Update fetch FAILED ", it)
            }
    }

    fun dbFetchFavorites(user: String, favoritesList: MutableLiveData<MutableList<FavoriteData>>){
        val Favoritesresults = mutableListOf<FavoriteData>()

        db.collection(collectionFavorites).whereEqualTo("uid",user).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Favorites fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                result.documents.mapNotNull {
                    Favoritesresults.add(it.toObject(FavoriteData::class.java)!!)
                }
                favoritesList.postValue(Favoritesresults)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "Favorites fetch FAILED ", it)
            }
    }

    fun dbUpdateFavorites(fav: FavoriteData){
        db.collection(collectionFavorites).whereEqualTo("uid",fav.uid).whereEqualTo("stock_ticker",fav.stock_ticker).get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "Favorites Update fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                if(result!!.documents.size > 0)
                {
                    db.collection(collectionFavorites).document(result.documents[0].id).delete().addOnSuccessListener {
                        MainViewModel.fetchFavoritesData(fav.uid)
                    }
                }
                else
                {
                    fav.firestoreID = db.collection(collectionFavorites).document().id
                    db.collection(collectionFavorites).document(fav.firestoreID).set(fav).addOnSuccessListener {
                        MainViewModel.fetchFavoritesData(fav.uid)
                    }
                }

            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "Favorites Update fetch FAILED ", it)
            }
    }
}