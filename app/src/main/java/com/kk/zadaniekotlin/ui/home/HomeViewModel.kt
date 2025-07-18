package com.kk.zadaniekotlin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kk.zadaniekotlin.ui.home.HomeUiState

class HomeViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_IMAGE_URLS = "imageUrls"
        private const val KEY_SELECTED_CATEGORY_ID = "selectedCategoryId"
        private const val KEY_SELECTED_CATEGORY_NAME = "selectedCategoryName"
    }

    private val _uiState = MutableLiveData<HomeUiState>()
    val uiState: LiveData<HomeUiState> get() = _uiState

    init {
        val cachedUrls = savedStateHandle.get<List<String>>(KEY_IMAGE_URLS)
        if (cachedUrls != null) {
            _uiState.value = if (cachedUrls.isEmpty()) {
                HomeUiState.Empty
            } else {
                HomeUiState.Success(cachedUrls)
            }
        } else {
            loadImageUrls()
        }
    }

    fun loadImageUrls() {
        _uiState.value = HomeUiState.Loading

        val urlRef = FirebaseDatabase.getInstance().getReference("imageUrls")
        urlRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val urls = mutableListOf<String>()
                for (child in snapshot.children) {
                    val url = child.getValue(String::class.java)
                    url?.let { urls.add(it) }
                }

                savedStateHandle[KEY_IMAGE_URLS] = urls

                _uiState.value = if (urls.isEmpty()) {
                    HomeUiState.Empty
                } else {
                    HomeUiState.Success(urls)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Błąd: ${error.message}")
                _uiState.value = HomeUiState.Error(error.message ?: "Błąd ładowania danych")
            }
        })
    }

    fun setSelectedCategory(id: Int, name: String) {
        savedStateHandle[KEY_SELECTED_CATEGORY_ID] = id
        savedStateHandle[KEY_SELECTED_CATEGORY_NAME] = name
    }

    fun getSelectedCategoryId(): Int = savedStateHandle[KEY_SELECTED_CATEGORY_ID] ?: 6
    fun getSelectedCategoryName(): String? = savedStateHandle[KEY_SELECTED_CATEGORY_NAME]
}
