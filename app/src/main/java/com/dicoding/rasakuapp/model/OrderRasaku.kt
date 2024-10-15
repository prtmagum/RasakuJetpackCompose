package com.dicoding.rasakuapp.model

data class OrderRasaku (
    val rasaku: Rasaku,
    val count: Int,
    var isFavorite: Boolean = false
)