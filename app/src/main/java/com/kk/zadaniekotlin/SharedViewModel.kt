package com.kk.zadaniekotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class SharedViewModel @Inject constructor() : ViewModel() {

    private val _catId = MutableLiveData<Int>().apply { value = 0 }
    val catId: LiveData<Int> get() = _catId

    private val _subCatId = MutableLiveData<Int>().apply { value = 0 }
    val subCatId: LiveData<Int> get() = _subCatId

    fun setCatId(id: Int) {
        _catId.value = id
    }

    fun setSubCatId(id: Int) {
        _subCatId.value = id
    }
    fun resetCategorySelection() {
        _catId.value = 0
        _subCatId.value = 0
    }

}