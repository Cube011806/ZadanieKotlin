package com.kk.zadaniekotlin.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String, //ZMIENIĆ NA COŚ BEZPIECZNEGO!!!
    val basket: Basket = Basket()
)
