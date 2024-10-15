package com.dicoding.rasakuapp.ui.screen.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.rasakuapp.R
import com.dicoding.rasakuapp.di.Injection
import com.dicoding.rasakuapp.model.OrderRasaku
import com.dicoding.rasakuapp.ui.ViewModelFactory
import com.dicoding.rasakuapp.ui.common.UiState
import com.dicoding.rasakuapp.ui.components.EmptyList
import com.dicoding.rasakuapp.ui.components.FavoriteItem

@Composable
fun FavoriteScreen(
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    )
) {
    val uiState = viewModel.uiState.collectAsState(initial = UiState.Loading).value

    when (uiState) {
        is UiState.Loading -> {
            viewModel.getFavoriteRasaku()
        }

        is UiState.Success -> {
            FavoriteContent(
                listRasaku = uiState.data,
                navigateToDetail = navigateToDetail,
                onFavoriteIconClicked = { id, newState ->
                    viewModel.updateFavoriteRasaku(id, newState)
                }
            )
        }

        is UiState.Error -> {
            // Handle error state (e.g., show a message)
        }
    }
}

@Composable
fun FavoriteContent(
    listRasaku: List<OrderRasaku>,
    navigateToDetail: (Long) -> Unit,
    onFavoriteIconClicked: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box( // Gunakan Box untuk memungkinkan posisi komponen di tengah
        modifier = modifier
            .fillMaxSize() // Memenuhi seluruh ukuran layar
    ){
        if (listRasaku.isNotEmpty()) {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 2.dp)
            ){
                listRasaku.forEach { orderRasaku ->
                    FavoriteItem(
                        rasakuId = orderRasaku.rasaku.id,
                        image = orderRasaku.rasaku.image,
                        title = orderRasaku.rasaku.title,
                        totalPoint = orderRasaku.rasaku.price,
                        isFavorite = orderRasaku.isFavorite,
                        onFavoriteIconClicked = onFavoriteIconClicked,
                        onClick = {
                            navigateToDetail(orderRasaku.rasaku.id) // Navigasi ke halaman detail
                        },
                        modifier = Modifier.clickable {
                            navigateToDetail(orderRasaku.rasaku.id)
                        }
                    )
                }
            }
        } else {
            EmptyList(
                Warning = stringResource(R.string.empty_favorite),
            )
        }
    }
}


