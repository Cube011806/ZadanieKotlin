package com.kk.zadaniekotlin.ui.dashboard

import androidx.lifecycle.*
import com.google.firebase.database.*
import com.kk.zadaniekotlin.model.Item

class DashboardViewModel : ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private val _uiState = MutableLiveData<DashboardUiState>()
    val uiState: LiveData<DashboardUiState> get() = _uiState

    private val databaseRef = FirebaseDatabase.getInstance().getReference("items")

    fun loadItems(catId: Int, subCatId: Int) {
        _uiState.value = DashboardUiState.Loading

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allItems = snapshot.children.mapNotNull { it.getValue(Item::class.java) }

                val filteredItems = allItems.filter { item ->
                    (catId == 0 || item.catId == catId) &&
                            (subCatId == 0 || item.subCatId == subCatId)
                }

                _items.value = filteredItems

                _uiState.value = if (filteredItems.isEmpty()) {
                    DashboardUiState.Empty
                } else {
                    DashboardUiState.Success
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.value = DashboardUiState.Error(Exception(error.message))
            }
        })
    }
}

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    object Success : DashboardUiState()
    object Empty : DashboardUiState()
    data class Error(val exception: Exception) : DashboardUiState()
}