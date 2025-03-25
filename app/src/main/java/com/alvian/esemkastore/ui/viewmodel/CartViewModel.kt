package com.alvian.esemkastore.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvian.esemkastore.model.CartItem
import com.alvian.esemkastore.model.Item

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    fun addToCart(item: Item, quantity: Int, totalPrice: Long) {
        val currentItems = _cartItems.value.orEmpty().toMutableList()
        currentItems.add(
            CartItem(
                item = item,
                quantity = quantity,
                totalPrice = totalPrice
            )
        )
        _cartItems.value = currentItems
    }

    fun getCartCount(): Int = _cartItems.value?.size ?: 0

    fun checkout() {
        _cartItems.value = emptyList()
    }

}