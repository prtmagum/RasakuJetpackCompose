package com.dicoding.rasakuapp.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.rasakuapp.data.RasakuRepository
import com.dicoding.rasakuapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel (
private val repository: RasakuRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CartState>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CartState>>
    get() = _uiState

    fun getAddedOrderRasaku() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAddedOrderRasaku()
                .collect { orderRasaku ->
                    val totalprice =
                        orderRasaku.sumOf { it.rasaku.price * it.count }
                    _uiState.value = UiState.Success(CartState(orderRasaku, totalprice))
                }
        }
    }

    fun updateOrderRasaku(rasakuId: Long, count: Int) {
        viewModelScope.launch {
            repository.updateOrderRasaku(rasakuId, count)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getAddedOrderRasaku()
                    }
                }
        }
    }
}