package com.alvian.esemkastore.model

data class CartItem(
    val item: Item,
    val quantity: Int,
    val totalPrice: Long
)
