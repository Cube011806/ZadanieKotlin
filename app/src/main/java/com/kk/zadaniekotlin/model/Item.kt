package com.kk.zadaniekotlin.model

data class Item(
    val title: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val catId: Int = 0,
    val subCatId: Int = 0
)
