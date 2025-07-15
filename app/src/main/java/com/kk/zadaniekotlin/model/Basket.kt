package com.kk.zadaniekotlin.model

data class Basket(
    val items: MutableList<Item> = mutableListOf()
) {
    fun addItem(item: Item) {
        items.add(item)
    }

}
