package com.capstone.trendfits.ui.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.trendfits.R
import com.capstone.trendfits.di.Injection
import com.capstone.trendfits.model.ClothesOrder
import com.capstone.trendfits.ui.ViewModelFactory
import com.capstone.trendfits.ui.components.OutfitsDetailList
import com.capstone.trendfits.ui.components.StateUi

@Composable
fun DetailOutfits(
    clothesId: Long,
    viewModel: DetailClothesViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    clothesOrder: List<ClothesOrder>,
    navigateToOutfitDetail: (Long) -> Unit,
    navigateBack: () -> Unit,
) {
    viewModel.uiState.collectAsState(initial = StateUi.Loading).value.let { stateUi ->
        when (stateUi) {
            is StateUi.Loading -> {
                viewModel.getClothesById(clothesId)
            }
            is StateUi.Success -> {
                val data = stateUi.data
                DetailOutfitsContent(
                    image = data.clothes.image,
                    clothesOrder = clothesOrder,
                    navigateToOutfitDetail = navigateToOutfitDetail,
                    onBackClick = navigateBack
                )
            }
            is StateUi.Error -> {}
        }
    }
}

@Composable
fun DetailOutfitsContent(
    @DrawableRes image: Int,
    clothesOrder: List<ClothesOrder>,
    navigateToOutfitDetail: (Long) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Box {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .height(400.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 20.dp))
            )
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .padding(20.dp)
                    .clickable { onBackClick() }
            )
        }
        OutfitsDetailList(
            outfitsOrder = clothesOrder, navigateToOutfitDetail = navigateToOutfitDetail,
        )
    }
}