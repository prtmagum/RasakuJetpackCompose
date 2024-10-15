package com.dicoding.rasakuapp.ui.screen.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.rasakuapp.R
import com.dicoding.rasakuapp.di.Injection
import com.dicoding.rasakuapp.ui.ViewModelFactory
import com.dicoding.rasakuapp.ui.common.UiState
import com.dicoding.rasakuapp.ui.components.OrderButton
import com.dicoding.rasakuapp.ui.components.ProductCounter
import com.dicoding.rasakuapp.ui.theme.RasakuAppTheme

@Composable
fun DetailScreen(
    rasakuId: Long,
    viewModel: DetailRasakuViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getRasakuById(rasakuId)
            }
            is UiState.Success -> {
                val data = uiState.data
                DetailContent(
                    data.rasaku.image,
                    data.rasaku.title,
                    data.rasaku.price,
                    data.count,
                    data.rasaku.description,
                    onBackClick = navigateBack,
                    onAddToCart = { count ->
                        viewModel.addToCart(data.rasaku, count)
                        navigateToCart()
                    },
                    id = data.rasaku.id, // Tambahkan ID rasaku di sini
                    isFavorite = data.isFavorite ,
                    onFavoriteButtonClicked = { id, state ->
                        viewModel.updateFavoriteStatus(id, state)
                    }// Tambahkan status favorite di sini
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun DetailContent(
    @DrawableRes image: Int,
    title: String,
    basePrice: Int,
    count: Int,
    description: String,
    onBackClick: () -> Unit,
    onAddToCart: (count: Int) -> Unit,
    onFavoriteButtonClicked: (id: Long, state: Boolean) -> Unit,
    id: Long,
    isFavorite: Boolean, // Ambil dari ViewModel
    modifier: Modifier = Modifier,
) {
    var totalPoint by rememberSaveable { mutableStateOf(0) }
    var orderCount by rememberSaveable { mutableStateOf(count) }
    var favoriteState by remember { mutableStateOf(isFavorite) } // Gunakan remember untuk menyimpan status favorit lokal

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier.height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier.padding(16.dp).clickable { onBackClick() }
                )

                IconButton(
                    onClick = {
                        favoriteState = !favoriteState // Update status favorit secara lokal
                        onFavoriteButtonClicked(id, favoriteState) // Panggil callback untuk update di ViewModel
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .size(40.dp)
                        .background(Color.White)
                        .testTag("favorite_detail_button")
                ) {
                    Icon(
                        imageVector = if (favoriteState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (favoriteState) stringResource(R.string.delete_favorite) else stringResource(R.string.add_favorite),
                        tint = if (favoriteState) Color.Red else Color.Black // Update warna berdasarkan status lokal favorit
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                Text(
                    text = stringResource(R.string.price_rasaku, basePrice),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = description,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium,

                )
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().height(4.dp).background(LightGray))

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ProductCounter(
                1,
                orderCount,
                onProductIncreased = { orderCount++ },
                onProductDecreased = { if (orderCount > 0) orderCount-- },
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
            )
            totalPoint = basePrice * orderCount
            OrderButton(
                text = stringResource(R.string.add_to_cart, totalPoint),
                enabled = orderCount > 0,
                onClick = {
                    onAddToCart(orderCount)
                }
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_3A)
@Composable
fun DetailContentPreview() {
    RasakuAppTheme {
        DetailContent(
            R.drawable.rasaku_4,
            "Lepet Isi Kacang",
            10000,
            1,
            "Gemblong adalah camilan tradisional khas Kuningan yang terbuat dari ketan dan gula merah. Teksturnya yang renyah di luar dan kenyal di dalam menjadikannya cemilan yang digemari oleh berbagai kalangan.",
            onBackClick = {},
            onAddToCart = {},
            onFavoriteButtonClicked = { _, _ -> }, // Lambda dummy untuk preview
            id = 1L, // Dummy ID untuk preview
            isFavorite = false // Status favorite dummy untuk preview
        )
    }
}