package com.dicoding.rasakuapp.model

data class Rasaku(
    val id: Long,
    val image: Int,
    val title: String,
    val price: Int,
    val description: String,
    var isFavorite: Boolean = false
)
