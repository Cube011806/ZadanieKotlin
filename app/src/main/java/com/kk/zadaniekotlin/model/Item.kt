package com.kk.zadaniekotlin.model

data class Item(
    val title: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val catId: Int = 0,
    val subCatId: Int = 0
)
