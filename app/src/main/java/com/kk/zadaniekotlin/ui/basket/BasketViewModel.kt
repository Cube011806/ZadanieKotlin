package com.kk.zadaniekotlin.ui.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kk.zadaniekotlin.model.Item

class BasketViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<Item>>(emptyList())
    val cartItems: LiveData<List<Item>> get() = _cartItems

    private val _cartSum = MutableLiveData<Double>(0.0)
    val cartSum: LiveData<Double> get() = _cartSum

    private val _uiState = MutableLiveData<BasketUiState>()
    val uiState: LiveData<BasketUiState> get() = _uiState

    fun addItem(item: Item) {
        val updated = _cartItems.value?.toMutableList() ?: mutableListOf()
        updated.add(item)
        _cartItems.value = updated
        _cartSum.value = updated.sumOf { it.price ?: 0.0 }
        saveCartToFirebase()
    }

    fun removeItem(item: Item) {
        val updated = _cartItems.value?.toMutableList() ?: mutableListOf()
        updated.remove(item)
        _cartItems.value = updated
        _cartSum.value = updated.sumOf { it.price ?: 0.0 }
        saveCartToFirebase()
    }

    fun saveCartToFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val cartItems = _cartItems.value ?: return
        val ref = FirebaseDatabase.getInstance().getReference("carts").child(userId)
        ref.setValue(cartItems)
    }

    fun loadCartFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("carts").child(userId)

        _uiState.value = BasketUiState.Loading

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedItems = snapshot.children.mapNotNull { it.getValue(Item::class.java) }
                _cartItems.value = loadedItems
                _cartSum.value = loadedItems.sumOf { it.price ?: 0.0 }

                _uiState.value = if (loadedItems.isEmpty()) {
                    BasketUiState.Empty
                } else {
                    BasketUiState.Success(loadedItems)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.value = BasketUiState.Error(error.message)
            }
        })
    }
}
sealed class BasketUiState {
    data object Loading : BasketUiState()
    data class Success(val basket: List<Item>) : BasketUiState()
    data object Empty : BasketUiState()
    data class Error(val message: String) : BasketUiState()
}