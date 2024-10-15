package com.dicoding.rasakuapp.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.rasakuapp.data.RasakuRepository
import com.dicoding.rasakuapp.model.OrderRasaku
import com.dicoding.rasakuapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: RasakuRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<OrderRasaku>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<OrderRasaku>>>
        get() = _uiState

    fun getFavoriteRasaku() = viewModelScope.launch {
        repository.getFavoriteRasaku()
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateFavoriteRasaku(rasakuId: Long, newState: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteRasaku(rasakuId, newState)
                .catch { exception ->
                    _uiState.value = UiState.Error(exception.message.toString())
                }
                .collect { result ->
                    if (result) {
                        getFavoriteRasaku() // Refresh favorite list after update
                    }
                }
        }
    }
}




