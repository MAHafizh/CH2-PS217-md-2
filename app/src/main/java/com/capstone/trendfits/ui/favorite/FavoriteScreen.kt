package com.capstone.trendfits.ui.favorite

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.capstone.trendfits.R
import com.capstone.trendfits.di.Injection
import com.capstone.trendfits.model.ClothesOrder
import com.capstone.trendfits.ui.ViewModelFactory
import com.capstone.trendfits.ui.components.FavoriteGrid
import com.capstone.trendfits.ui.components.StateUi
import com.capstone.trendfits.ui.signin.UserData
@SuppressLint("ModifierParameter")
@Composable
fun FavoriteScreen(
    userData: UserData?,
    viewModel: FavoriteScreenViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
) {
    viewModel.stateUi.collectAsState(initial = StateUi.Loading).value.let { stateUi ->
        when (stateUi) {
            is StateUi.Loading -> {
                viewModel.getAllClothes()
            }

            is StateUi.Success -> {
                FavoriteContent(
                    userData = userData,
                    favorites = stateUi.data,
                    modifier = modifier,
                    navigateToDetail = navigateToDetail,
                )
            }

            is StateUi.Error -> {}
        }
    }
}

@Composable
fun FavoriteContent(
    userData: UserData?,
    favorites: List<ClothesOrder>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
){
    Scaffold {
            innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = modifier
                    .height(58.dp)
                    .padding(bottom = 16.dp)
            )
            Box(
                modifier = modifier
                    .size(68.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(userData?.profilePictureUrl),
                    contentDescription = "profile",
                    modifier = modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = userData?.username.toString(),
                modifier = modifier.padding(top = 8.dp, bottom = 20.dp)
            )
            FavoriteGrid(
                clothesOrder = favorites,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun SettingContent(
//    userData: UserData?,
//    modifier: Modifier = Modifier,
//) {
//    Scaffold(
//        content = {
//            Column(
//                modifier = modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.logo),
//                    contentDescription = null,
//                    modifier = modifier
//                        .height(58.dp)
//                        .padding(bottom = 16.dp)
//                )
//                Box(
//                    modifier = modifier
//                        .size(68.dp)
//                        .clip(CircleShape)
//                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter(userData?.profilePictureUrl),
//                        contentDescription = "profile",
//                        modifier = modifier
//                            .fillMaxSize(),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//                Text(
//                    text = userData?.username.toString(),
//                    modifier = modifier.padding(top = 8.dp, bottom = 20.dp)
//                )
//            }
//        }
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun SettingPreview() {
//    TrendFitsTheme {
//        FavoriteScreen()
//    }
//}