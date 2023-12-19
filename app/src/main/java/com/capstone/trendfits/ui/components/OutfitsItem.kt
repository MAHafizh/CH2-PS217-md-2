package com.capstone.trendfits.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.capstone.trendfits.R
import com.capstone.trendfits.ui.theme.TrendFitsTheme

@Composable
fun OutfitsItem(
    imageId: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(170.dp)
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp, topStart = 30.dp, topEnd = 30.dp))
        )
    }
}

@Composable
@Preview(showBackground = true)
fun OutfitsItemPreview() {
    TrendFitsTheme {
        OutfitsItem(R.drawable.image1)
    }
}