package com.kk.zadaniekotlin.ui.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kk.zadaniekotlin.model.Item
import javax.inject.Inject

class BasketViewModel @Inject constructor() : ViewModel() {

    private val _cartItems = MutableLiveData<List<Item>>(emptyList())
    val cartItems: LiveData<List<Item>> get() = _cartItems

    private val _cartSum = MutableLiveData(0.0)
    val cartSum: LiveData<Double> get() = _cartSum

    private val _uiState = MutableLiveData<BasketUiState>()
    val uiState: LiveData<BasketUiState> get() = _uiState

    fun addItem(item: Item) {
        updateCart { items -> items + item }
    }

    fun removeItem(item: Item) {
        updateCart { items -> items - item }
    }

    private fun updateCart(modifier: (List<Item>) -> List<Item>) {
        val updated = modifier(_cartItems.value ?: emptyList())
        _cartItems.value = updated
        _cartSum.value = updated.sumOf { it.price }
        saveCartToFirebase(updated)
    }

    fun saveCartToFirebase(cart: List<Item> = _cartItems.value ?: emptyList()) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance()
            .getReference("carts")
            .child(userId)
            .setValue(cart)
    }

    fun loadCartFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("carts").child(userId)

        _uiState.value = BasketUiState.Loading

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedItems = snapshot.children.mapNotNull { it.getValue(Item::class.java) }
                _cartItems.value = loadedItems
                _cartSum.value = loadedItems.sumOf { it.price }

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
