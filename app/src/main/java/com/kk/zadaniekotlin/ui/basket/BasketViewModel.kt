package com.kk.zadaniekotlin.ui.basket

import androidx.compose.ui.graphics.findFirstRoot
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kk.zadaniekotlin.model.Item

class BasketViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<Item>>(emptyList())
    val cartItems: LiveData<List<Item>> get() = _cartItems
    private val _cartSum = MutableLiveData<Double>(0.0)
    val cartSum: LiveData<Double> get() = _cartSum

    private val _uiState = MutableLiveData<BasketUiState>()
    val uiState: LiveData<BasketUiState> get() = _uiState

    fun addItem(item: Item) {
        val updatedList = _cartItems.value?.toMutableList() ?: mutableListOf()
        updatedList.add(item)
        _cartItems.value = updatedList

        val total = updatedList.sumOf { it.price ?: 0.0 }
        _cartSum.value = total
    }

    fun removeItem(item: Item) {
        val updatedList = _cartItems.value?.toMutableList() ?: mutableListOf()
        updatedList.remove(item)
        _cartItems.value = updatedList

        val total = updatedList.sumOf { it.price ?: 0.0 }
        _cartSum.value = total
    }


/*
        _uiState.value = if (updatedList.isEmpty()) {
            BasketUiState.Empty
        } else {
            BasketUiState.Success(updatedList)
        }*/

}