package com.kk.zadaniekotlin.ui.categories

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.database.*
import com.kk.zadaniekotlin.model.Category

class CategoryViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        private const val KEY_ID = "categoryId"
        private const val KEY_NAME = "categoryName"
    }

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    fun loadCategories(categoryId: Int) {
        savedStateHandle[KEY_ID] = categoryId
        val ref: DatabaseReference = when (categoryId) {
            1 -> FirebaseDatabase.getInstance().getReference("FemCategories")
            2 -> FirebaseDatabase.getInstance().getReference("MaleCategories")
            3 -> FirebaseDatabase.getInstance().getReference("BabyCategories")
            4 -> FirebaseDatabase.getInstance().getReference("GirlCategories")
            5 -> FirebaseDatabase.getInstance().getReference("BoyCategories")
            else -> FirebaseDatabase.getInstance().getReference("categories")
        }

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Category>()
                for (child in snapshot.children) {
                    val category = child.getValue(Category::class.java)
                    category?.let { result.add(it) }
                }
                _categories.value = result
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CategoryViewModel", "Błąd ładowania kategorii: ${error.message}")
            }
        })
    }

    fun getCurrentCategoryId(): Int = savedStateHandle[KEY_ID] ?: 6
    fun getCurrentCategoryName(): String? = savedStateHandle[KEY_NAME]
    fun setCurrentCategory(id: Int, name: String) {
        savedStateHandle[KEY_ID] = id
        savedStateHandle[KEY_NAME] = name
    }
}
