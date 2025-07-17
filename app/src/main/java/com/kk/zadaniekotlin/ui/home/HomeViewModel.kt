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

class HomeViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_IMAGE_URLS = "imageUrls"
        private const val KEY_SELECTED_CATEGORY_ID = "selectedCategoryId"
        private const val KEY_SELECTED_CATEGORY_NAME = "selectedCategoryName"
    }

    private val _imageUrls = MutableLiveData<List<String>>()
    val imageUrls: LiveData<List<String>> = _imageUrls

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        val cachedUrls = savedStateHandle.get<List<String>>(KEY_IMAGE_URLS)
        if (cachedUrls != null) {
            _imageUrls.value = cachedUrls
        } else {
            loadImageUrls()
        }
    }

    fun saveImageUrls(urls: List<String>) {
        savedStateHandle[KEY_IMAGE_URLS] = urls
        _imageUrls.value = urls
    }

    fun getSavedImageUrls(): List<String> {
        return savedStateHandle[KEY_IMAGE_URLS] ?: emptyList()
    }

    private fun loadImageUrls() {
        _loading.value = true
        val urlRef = FirebaseDatabase.getInstance().getReference("imageUrls")

        urlRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val urls = mutableListOf<String>()
                for (child in snapshot.children) {
                    val url = child.getValue(String::class.java)
                    url?.let { urls.add(it) }
                }
                saveImageUrls(urls)
                _loading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _loading.value = false
                Log.e("Firebase", "Błąd: ${error.message}")
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
