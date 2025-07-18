package com.kk.zadaniekotlin.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.database.*
import com.kk.zadaniekotlin.model.Item

class DashboardViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    fun loadItems(catId: Int, subCatId: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("items")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allItems = snapshot.children
                    .mapNotNull { it.getValue(Item::class.java) }

                val filtered = if (catId != 0 && subCatId != 0) {
                    allItems.filter { it.catId == catId && it.subCatId == subCatId }
                } else {
                    allItems
                }

                _items.value = filtered
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DashboardViewModel", "Błąd: ${error.message}")
            }
        })
    }

}
