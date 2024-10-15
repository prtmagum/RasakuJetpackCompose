package com.dicoding.rasakuapp.ui.screen.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.rasakuapp.R
import com.dicoding.rasakuapp.di.Injection
import com.dicoding.rasakuapp.ui.ViewModelFactory
import com.dicoding.rasakuapp.ui.common.UiState
import com.dicoding.rasakuapp.ui.components.CartItem
import com.dicoding.rasakuapp.ui.components.EmptyList
import com.dicoding.rasakuapp.ui.components.OrderButton

@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    onOrderButtonClicked: (String) -> Unit,
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAddedOrderRasaku()
            }

            is UiState.Success -> {
                CartContent(
                    uiState.data,
                    onProductCountChanged = { rasakuId, count ->
                        viewModel.updateOrderRasaku(rasakuId, count)
                    },
                    onOrderButtonClicked = onOrderButtonClicked
                )
            }

            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    state: CartState,
    onProductCountChanged: (id: Long, count: Int) -> Unit,
    onOrderButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shareMessage = stringResource(
        R.string.share_message,
        state.orderRasaku.count(),
        state.totalRequiredPrice
    )
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (state.orderRasaku.isEmpty()) {
            // Gunakan komponen EmptyList jika keranjang kosong
            EmptyList(
                Warning = stringResource(R.string.empty_cart_message) // Pesan kosong untuk keranjang
            )
        }else{
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(weight = 1f)
                ) {
                    items(state.orderRasaku, key = { it.rasaku.id }) { item ->
                        CartItem(
                            rasakuId = item.rasaku.id,
                            image = item.rasaku.image,
                            title = item.rasaku.title,
                            totalPoint = item.rasaku.price * item.count,
                            count = item.count,
                            onProductCountChanged = onProductCountChanged,
                        )
                        HorizontalDivider()
                    }
                }
            }
        OrderButton(
            text = stringResource(R.string.total_order, state.totalRequiredPrice),
            enabled = state.orderRasaku.isNotEmpty(),
            onClick = {
                onOrderButtonClicked(shareMessage)
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}