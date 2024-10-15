package com.dicoding.rasakuapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.rasakuapp.data.RasakuRepository
import com.dicoding.rasakuapp.ui.screen.cart.CartViewModel
import com.dicoding.rasakuapp.ui.screen.detail.DetailRasakuViewModel
import com.dicoding.rasakuapp.ui.screen.favorite.FavoriteViewModel
import com.dicoding.rasakuapp.ui.screen.home.HomeViewModel

class ViewModelFactory(private val repository: RasakuRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailRasakuViewModel::class.java)) {
            return DetailRasakuViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}