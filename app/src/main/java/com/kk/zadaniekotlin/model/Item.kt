package com.kk.zadaniekotlin.model

data class Item(
    val title: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val catId: Int,
    val subCatId: Int
)
