package com.kk.zadaniekotlin.view

import com.kk.zadaniekotlin.model.Item

interface ItemView {
    fun showItems(items: List<Item>)
}
