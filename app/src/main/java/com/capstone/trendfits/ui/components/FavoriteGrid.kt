package com.capstone.trendfits.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.capstone.trendfits.model.ClothesOrder

@Composable
fun FavoriteGrid(
    clothesOrder: List<ClothesOrder>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit
){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(140.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        modifier = modifier
    ) {
        items(clothesOrder) { data ->
            ClothesItem(
                image = data.clothes.image,
                title = data.clothes.title,
                modifier = Modifier.clickable {
                    navigateToDetail(data.clothes.id)
                }
            )
        }
    }
}