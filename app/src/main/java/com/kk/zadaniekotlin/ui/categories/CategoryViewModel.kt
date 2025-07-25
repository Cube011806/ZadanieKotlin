package com.kk.zadaniekotlin.ui.categories

import androidx.lifecycle.*
import com.google.firebase.database.*
import com.kk.zadaniekotlin.SharedViewModel
import com.kk.zadaniekotlin.model.Category

class CategoryViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_ID = "categoryId"
        private const val KEY_NAME = "categoryName"
    }

    private val databaseReferences = mapOf(
        1 to "FemCategories",
        2 to "MaleCategories",
        3 to "BabyCategories",
        4 to "GirlCategories",
        5 to "BoyCategories"
    )

    val categoryIdToName = mapOf(
        1 to "Kobiety",
        2 to "Mężczyźni",
        3 to "Niemowlak",
        4 to "Dziewczynka",
        5 to "Chłopiec",
        0 to "Wszystkie"
    )

    val categoryNameToId = categoryIdToName.entries.associate { (id, name) -> name to id }

    private val _uiState = MutableLiveData<CategoryUiState>()
    val uiState: LiveData<CategoryUiState> = _uiState

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _navigateToDashboard = MutableLiveData<Boolean?>()
    val navigateToDashboard: LiveData<Boolean?> get() = _navigateToDashboard

    fun loadCategories(categoryId: Int) {
        savedStateHandle[KEY_ID] = categoryId
        _uiState.value = CategoryUiState.Loading

        val refPath = databaseReferences[categoryId] ?: "items"
        val ref = FirebaseDatabase.getInstance().getReference(refPath)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children.mapNotNull { it.getValue(Category::class.java) }
                if (result.isEmpty()) {
                    _uiState.value = CategoryUiState.Empty
                } else {
                    _categories.value = result
                    _uiState.value = CategoryUiState.Success(result)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.value = CategoryUiState.Error(error.message)
            }
        })
    }

    fun getCurrentCategoryId(): Int = savedStateHandle[KEY_ID] ?: 0
    fun getCurrentCategoryName(): String? = savedStateHandle[KEY_NAME]

    fun setCurrentCategory(id: Int, name: String) {
        savedStateHandle[KEY_ID] = id
        savedStateHandle[KEY_NAME] = name
    }

    fun onCategorySelected(selectedName: String, sharedViewModel: SharedViewModel) {
        val selectedId = categoryNameToId[selectedName] ?: 0

        sharedViewModel.setCatId(selectedId)

        if (selectedId == 0) {
            sharedViewModel.setSubCatId(0)
            _navigateToDashboard.value = true
        } else {
            setCurrentCategory(selectedId, selectedName)
            loadCategories(selectedId)
            _navigateToDashboard.value = false
        }
    }

    fun resetNavigationFlag() {
        _navigateToDashboard.value = null
    }
}

sealed class CategoryUiState {
    data object Loading : CategoryUiState()
    data class Success(val categories: List<Category>) : CategoryUiState()
    data object Empty : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}