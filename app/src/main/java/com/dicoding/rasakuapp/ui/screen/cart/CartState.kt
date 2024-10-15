package com.dicoding.rasakuapp.ui.screen.cart

import com.dicoding.rasakuapp.model.OrderRasaku

data class CartState(
    val orderRasaku: List<OrderRasaku>,
    val totalRequiredPrice: Int
)