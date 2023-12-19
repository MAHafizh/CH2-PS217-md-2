package com.capstone.trendfits

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capstone.trendfits.ui.detail.DetailClothes
import com.capstone.trendfits.ui.detail.DetailOutfits
import com.capstone.trendfits.ui.favorite.FavoriteScreen
import com.capstone.trendfits.ui.home.HomeScreen
import com.capstone.trendfits.ui.navigation.NavigationItem
import com.capstone.trendfits.ui.navigation.Screen
import com.capstone.trendfits.ui.scan.Scan
import com.capstone.trendfits.ui.setting.SettingScreen
import com.capstone.trendfits.ui.signin.GoogleAuthUiClient
import com.capstone.trendfits.ui.signin.SignInScreen
import com.capstone.trendfits.ui.signin.SignInViewModel
import com.capstone.trendfits.ui.theme.TrendFitsTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrendFitsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        bottomBar = {
                            if (currentRoute != Screen.SignIn.route) {
                                BottomBar(navController)
                            }
                        },
                        modifier = Modifier
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.SignIn.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.SignIn.route) {
                                val viewModel = viewModel<SignInViewModel>()
                                val state by viewModel.state.collectAsState()

                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() != null) {
                                        navController.navigate(Screen.Home.route)
                                    }
                                }

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = { result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult =
                                                    googleAuthUiClient.signInWithIntent(
                                                        intent = result.data ?: return@launch
                                                    )
                                                viewModel.onSignInResult(signInResult)
                                            }
                                        }
                                    }
                                )

                                LaunchedEffect(key1 = state.isSignInSuccessful) {
                                    if (state.isSignInSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Sign in successful",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate(Screen.Home.route)
                                        viewModel.resetState()
                                    }
                                }

                                SignInScreen(
                                    state = state,
                                    onSignInClick = {
                                        lifecycleScope.launch {
                                            val signInIntentSender = googleAuthUiClient.signIn()
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    signInIntentSender ?: return@launch
                                                ).build()
                                            )
                                        }
                                    }
                                )
                            }
                            composable(Screen.Home.route) {
                                HomeScreen(
                                    userData = googleAuthUiClient.getSignedInUser(),
                                    navigateToDetail = { rewardId ->
                                        navController.navigate(
                                            Screen.DetailClothes.createRoute(
                                                rewardId
                                            )
                                        )
                                    },
                                    navigateToOutfitDetail = { outfitId ->
                                        navController.navigate(
                                            Screen.DetailOutfits.createRoute(
                                                outfitId
                                            )
                                        )
                                    }
                                )
                            }
                            composable(Screen.Scan.route) {
                                Scan()
                            }
                            composable(Screen.Favorite.route) {
                                FavoriteScreen(
                                    userData = googleAuthUiClient.getSignedInUser(),
                                )
                            }
                            composable(Screen.Setting.route) {
                                SettingScreen(
                                    userData = googleAuthUiClient.getSignedInUser(),
                                    onSignOut = {
                                        lifecycleScope.launch {
                                            googleAuthUiClient.signOut()
                                            Toast.makeText(
                                                applicationContext,
                                                "Signed out",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            navController.popBackStack(Screen.SignIn.route, false)
                                        }
                                    }
                                )
                            }
                            composable(
                                route = Screen.DetailClothes.route,
                                arguments = listOf(navArgument("clothesId") {
                                    type = NavType.LongType
                                }),
                            ) {
                                val id = it.arguments?.getLong("clothesId") ?: -1L
                                DetailClothes(
                                    clothesId = id,
                                    navigateBack = {
                                        navController.navigateUp()
                                    },
                                )
                            }
                            composable(
                                route = Screen.DetailOutfits.route,
                                arguments = listOf(navArgument("clothesId") {
                                    type = NavType.LongType
                                }),
                            ) {
                                val id = it.arguments?.getLong("clothesId") ?: -1L
                                DetailOutfits(
                                    clothesId = id,
                                    clothesOrder = emptyList(),
                                    navigateToOutfitDetail = { outfitId ->
                                        navController.navigate(
                                            Screen.DetailOutfits.createRoute(
                                                outfitId
                                            )
                                        )
                                    },
                                    navigateBack = {
                                        navController.navigateUp()
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    TrendFitsTheme {
        NavigationBar(
            modifier = modifier
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(MaterialTheme.colorScheme.primary),
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val navigationItems = listOf(
                NavigationItem(
                    title = stringResource(R.string.menu_home),
                    icon = Icons.Default.Home,
                    screen = Screen.Home
                ),
                NavigationItem(
                    title = stringResource(R.string.scan),
                    icon = Icons.Default.AddCircle,
                    screen = Screen.Scan
                ),
                NavigationItem(
                    title = stringResource(R.string.Favorite),
                    icon = Icons.Default.Favorite,
                    screen = Screen.Favorite
                ),
                NavigationItem(
                    title = stringResource(R.string.setting),
                    icon = Icons.Default.Settings,
                    screen = Screen.Setting
                ),
            )
            navigationItems.map { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomePreview() {
    TrendFitsTheme {
    }
}
