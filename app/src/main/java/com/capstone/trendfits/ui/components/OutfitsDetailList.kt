package com.capstone.trendfits.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.capstone.trendfits.model.ClothesOrder

@Composable
fun OutfitsDetailList(
    outfitsOrder: List<ClothesOrder>,
    modifier: Modifier = Modifier,
    navigateToOutfitDetail: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        items(outfitsOrder) { data ->
            OutfitsDetailItem(
                imageId = data.clothes.image,
                title = data.clothes.title,
                modifier = Modifier.clickable {
                    navigateToOutfitDetail(data.clothes.id)
                }
            )
        }
    }
}