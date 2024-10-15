package com.dicoding.rasakuapp.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.rasakuapp.R
import com.dicoding.rasakuapp.di.Injection
import com.dicoding.rasakuapp.model.OrderRasaku
import com.dicoding.rasakuapp.ui.ViewModelFactory
import com.dicoding.rasakuapp.ui.common.UiState
import com.dicoding.rasakuapp.ui.components.RasakuItem

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    val query = viewModel.query.collectAsState().value

    val uiState = viewModel.uiState.collectAsState(initial = UiState.Loading).value

    when (uiState) {
        is UiState.Loading -> {
            viewModel.getAllRasaku()
        }
        is UiState.Success -> {
            HomeContent(
                orderRasaku = uiState.data,
                query = query,
                onQueryChange = viewModel::search,
                modifier = modifier,
                navigateToDetail = navigateToDetail,
                onFavoriteIconClicked = { id, newState ->
                    viewModel.updateFavoriteRasaku(id, newState)
                },
            )
        }
        is UiState.Error -> {
            // Handle error state if necessary
        }
    }
}

@Composable
fun HomeContent(
    orderRasaku: List<OrderRasaku>,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    onFavoriteIconClicked: (id: Long, newState: Boolean) -> Unit
) {
    Column(modifier = modifier) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier.testTag("ProdukList")
        ) {
            items(orderRasaku) { data ->
                RasakuItem(
                    image = data.rasaku.image,
                    title = data.rasaku.title,
                    priceRasaku = data.rasaku.price,
                    modifier = Modifier.clickable {
                        navigateToDetail(data.rasaku.id)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {}, // Optionally add search action
        active = false,
        onActiveChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        placeholder = {
            Text(stringResource(R.string.search_rasaku))
        },
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .heightIn(min = 48.dp)){}
}
