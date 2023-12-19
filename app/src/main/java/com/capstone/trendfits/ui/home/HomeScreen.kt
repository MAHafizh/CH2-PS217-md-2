package com.capstone.trendfits.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.capstone.trendfits.di.Injection
import com.capstone.trendfits.model.ClothesOrder
import com.capstone.trendfits.ui.ViewModelFactory
import com.capstone.trendfits.ui.components.HomeSection
import com.capstone.trendfits.ui.components.Search
import com.capstone.trendfits.ui.components.StateUi
import com.capstone.trendfits.ui.components.WardrobeGrid
import com.capstone.trendfits.ui.signin.UserData
import com.capstone.trendfits.ui.theme.TrendFitsTheme

@Composable
fun HomeScreen(
    userData: UserData?,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit
) {

    var selectedTabIndex by remember { mutableStateOf(0) }

    viewModel.stateUi.collectAsState(initial = StateUi.Loading).value.let { stateUi ->
        when (stateUi) {
            is StateUi.Loading -> {
                viewModel.getAllClothes()
            }

            is StateUi.Success -> {
                ClothesContent(
                    userData = userData,
                    clothesOrder = stateUi.data,
                    modifier = modifier,
                    navigateToDetail = navigateToDetail,
                    selectedTabIndex = selectedTabIndex
                ) { index ->
                    selectedTabIndex = index
                }
            }

            is StateUi.Error -> {}
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClothesContent(
    userData: UserData?,
    clothesOrder: List<ClothesOrder>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {

    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HomeSection(
                    title = "Hi, ${userData?.username}",
                )
                Box(
                    modifier = modifier
                        .size(45.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(userData?.profilePictureUrl),
                        contentDescription = "profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Text(
                text = "Discover your style",
            )
            Search()

            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { onTabSelected(0) },
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                ) {
                    Text(text = "Wardrobe")
                }
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { onTabSelected(1) },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                ) {
                    Text(text = "Outfit")
                }
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { onTabSelected(2) },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                ) {
                    Text(text = "Category")
                }
            }

            when (selectedTabIndex) {
                0 -> {
                    WardrobeGrid(
                        clothesOrder = clothesOrder,
                        navigateToDetail = navigateToDetail
                    )
                }
                1 -> {
                    Text(
                        text = "Outfit",
                    )
                }
                2 -> {
                    Text(
                        text = "Category",
                    )
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun HomePreview() {
    TrendFitsTheme {
//        HomeScreen(navigateToDetail = { /* Define a dummy lambda function */ })
    }
}