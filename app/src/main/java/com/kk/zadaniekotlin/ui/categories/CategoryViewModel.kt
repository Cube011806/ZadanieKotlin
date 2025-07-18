package com.kk.zadaniekotlin.ui.categories

import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.Navigation.findNavController
import com.google.firebase.database.*
import com.kk.zadaniekotlin.model.Category
import com.kk.zadaniekotlin.R
class CategoryViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_ID = "categoryId"
        private const val KEY_NAME = "categoryName"
    }
    private val _uiState = MutableLiveData<CategoryUiState>()
    val uiState: LiveData<CategoryUiState> get() = _uiState

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    fun loadCategories(categoryId: Int) {
        savedStateHandle[KEY_ID] = categoryId
        _uiState.value = CategoryUiState.Loading

        val ref: DatabaseReference = when (categoryId) {
            1 -> FirebaseDatabase.getInstance().getReference("FemCategories")
            2 -> FirebaseDatabase.getInstance().getReference("MaleCategories")
            3 -> FirebaseDatabase.getInstance().getReference("BabyCategories")
            4 -> FirebaseDatabase.getInstance().getReference("GirlCategories")
            5 -> FirebaseDatabase.getInstance().getReference("BoyCategories")
            else -> FirebaseDatabase.getInstance().getReference("items")
        }

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children.mapNotNull { it.getValue(Category::class.java) }

                _uiState.value = if (result.isEmpty()) {
                    CategoryUiState.Empty
                } else {
                    CategoryUiState.Success(result)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.value = CategoryUiState.Error(error.message ?: "Błąd ładowania kategorii")
            }
        })
    }


    fun getCurrentCategoryId(): Int = savedStateHandle[KEY_ID] ?: 6 //!!!!!!!!!!!
    fun getCurrentCategoryName(): String? = savedStateHandle[KEY_NAME]
    fun setCurrentCategory(id: Int, name: String) {
        savedStateHandle[KEY_ID] = id
        savedStateHandle[KEY_NAME] = name
    }
}
