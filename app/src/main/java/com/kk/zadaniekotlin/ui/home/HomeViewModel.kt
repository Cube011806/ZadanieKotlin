package com.kk.zadaniekotlin.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.database.*

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_IMAGE_URLS = "imageUrls"
        private const val KEY_SELECTED_CATEGORY_ID = "selectedCategoryId"
        private const val KEY_SELECTED_CATEGORY_NAME = "selectedCategoryName"
    }

    private val _uiState = MutableLiveData<HomeUiState>()
    val uiState: LiveData<HomeUiState> = _uiState

    val categories = listOf(
        1 to "Kobiety",
        2 to "Mężczyzna",
        3 to "Niemowlak",
        4 to "Dziewczynka",
        5 to "Chłopiec"
    )

    init {
        val cachedUrls = savedStateHandle.get<List<String>>(KEY_IMAGE_URLS)
        if (cachedUrls != null) {
            _uiState.value = if (cachedUrls.isEmpty()) HomeUiState.Empty
            else HomeUiState.Success(getCategoryImages(cachedUrls))
        } else {
            fetchImageUrls()
        }
    }

    private fun fetchImageUrls() {
        _uiState.value = HomeUiState.Loading

        FirebaseDatabase.getInstance().getReference("imageUrls")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val urls = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                    savedStateHandle[KEY_IMAGE_URLS] = urls
                    _uiState.value = if (urls.isEmpty()) HomeUiState.Empty
                    else HomeUiState.Success(getCategoryImages(urls))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HomeViewModel", "Firebase Error: ${error.message}")
                    _uiState.value = HomeUiState.Error(error.message)
                }
            })
    }

    fun getCategoryImages(imageUrls: List<String>): List<CategoryImage> {
        return categories.zip(imageUrls) { (id, name), url ->
            CategoryImage(id, name, url)
        }
    }

    fun onCategoryClicked(id: Int) {
        val name = getCategoryNameById(id) ?: "Nieznana"
        savedStateHandle[KEY_SELECTED_CATEGORY_ID] = id
        savedStateHandle[KEY_SELECTED_CATEGORY_NAME] = name
    }

    private fun getCategoryNameById(id: Int): String? = categories.find { it.first == id }?.second
}

data class CategoryImage(
    val id: Int,
    val name: String,
    val imageUrl: String
)

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val categoryImages: List<CategoryImage>) : HomeUiState()
    object Empty : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
