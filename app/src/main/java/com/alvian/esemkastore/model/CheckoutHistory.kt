package com.alvian.esemkastore.model

data class CheckoutHistory(
    val totalPrice: Long,
    val orderDate: String,
    val acceptanceDate: String,
    val details: List<CheckoutDetail>
)
