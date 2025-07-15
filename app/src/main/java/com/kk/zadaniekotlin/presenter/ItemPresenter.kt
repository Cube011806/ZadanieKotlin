package com.kk.zadaniekotlin.presenter

import com.kk.zadaniekotlin.model.ItemModel
import com.kk.zadaniekotlin.view.ItemView

class ItemPresenter(
    private val view: ItemView,
    private val model: ItemModel
) {
    fun loadItems() {
        val items = model.getItems()
        view.showItems(items)
    }
}
