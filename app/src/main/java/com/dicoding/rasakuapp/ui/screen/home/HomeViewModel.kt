package com.dicoding.rasakuapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.rasakuapp.data.RasakuRepository
import com.dicoding.rasakuapp.model.OrderRasaku
import com.dicoding.rasakuapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: RasakuRepository
) : ViewModel() {

    // State for storing the list of Rasaku items
    private val _uiState: MutableStateFlow<UiState<List<OrderRasaku>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<OrderRasaku>>>
        get() = _uiState

    // State for storing the search query
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    // Load all Rasaku items
    fun getAllRasaku() {
        viewModelScope.launch {
            repository.getAllRasaku()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { orderRasaku ->
                    // Filter the list based on the search query
                    val filteredRasaku = orderRasaku.filter { it.rasaku.title.contains(_query.value, ignoreCase = true) }
                    _uiState.value = UiState.Success(filteredRasaku)
                }
        }
    }

    // Function to update the query state
    fun search(newQuery: String) {
        _query.value = newQuery
        // Reload the list with the updated query
        getAllRasaku()
    }

    fun updateFavoriteRasaku(id: Long, newState: Boolean) = viewModelScope.launch {
        repository.updateFavoriteRasaku(id, newState)
            .collect { isUpdated ->
                if (isUpdated) search(_query.value)
            }
    }
}
