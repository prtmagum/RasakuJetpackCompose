package com.dicoding.rasakuapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.rasakuapp.data.RasakuRepository
import com.dicoding.rasakuapp.model.OrderRasaku
import com.dicoding.rasakuapp.model.Rasaku
import com.dicoding.rasakuapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailRasakuViewModel (
    private val repository: RasakuRepository
    ) : ViewModel() {
        private val _uiState: MutableStateFlow<UiState<OrderRasaku>> =
            MutableStateFlow(UiState.Loading)
        val uiState: StateFlow<UiState<OrderRasaku>>
        get() = _uiState

        fun getRasakuById(rasakuId: Long) {
            viewModelScope.launch {
                _uiState.value = UiState.Loading
                val rasakuData = repository.getOrderRasakuById(rasakuId)
                _uiState.value = UiState.Success(rasakuData)
            }
        }

        fun addToCart(rasaku: Rasaku, count: Int) {
            viewModelScope.launch {
                repository.updateOrderRasaku(rasaku.id, count)
            }
        }

    fun updateFavoriteStatus(rasakuId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteRasaku(rasakuId, isFavorite)
            getRasakuById(rasakuId) // Refresh the item to get updated state
        }
    }
}