package com.home.artz.view.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.home.artz.R
import com.home.artz.view.ar.ARScreen
import com.home.artz.view.details.DetailsScreen
import com.home.artz.view.discover.DiscoverScreen
import com.home.artz.view.favorite.FavoriteScreen
import com.home.artz.view.search.SearchScreen
import com.home.artz.view.ui.theme.ArtzTheme
import com.home.artz.viewmodel.ArtworkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val artworkViewModel: ArtworkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtzTheme {
                val mainNavController = rememberNavController()
                NavHost(
                    navController = mainNavController,
                    graph = mainNavController.createGraph(Screen.HOME.name, null) {
                        composable(Screen.HOME.name) {
                            val homeScreenNavController = rememberNavController()
                            Scaffold(bottomBar = {
                                BottomNavigationBar(homeScreenNavController)
                            }) { contentPadding ->
                                val artworks = remember {
                                    artworkViewModel.cachedArtworks
                                }
                                NavHost(
                                    navController = homeScreenNavController,
                                    graph = homeScreenNavController.createGraph(
                                        Screen.HOME_DISCOVER.name,
                                        null
                                    ) {
                                        composable(Screen.HOME_DISCOVER.name) {
                                            DiscoverScreen(
                                                artworks.value,
                                                contentPadding,
                                                artworkViewModel.showPagingLoader,
                                                { artwork ->
                                                    artworkViewModel.setSelectedArtwork(artwork)
                                                    mainNavController.navigate(Screen.DETAILS.name)
                                                },
                                                onFavoriteButtonClicked = { artwork, isFavorite ->
                                                    artworkViewModel.modifyFavoriteStateOn(
                                                        artwork,
                                                        isFavorite
                                                    )
                                                },
                                                onScrollEnded = {
                                                    artworkViewModel.loadNextPage()
                                                })
                                        }
                                        composable(Screen.HOME_AR.name) {
                                            ARScreen()
                                        }
                                        composable(Screen.HOME_SEARCH.name) {
                                            SearchScreen()
                                        }
                                        composable(Screen.HOME_FAVORITES.name) {
                                            val favorites = artworks.value.filter { it.isFavorite }
                                            FavoriteScreen(favorites,
                                                contentPadding,
                                                { index ->
                                                    artworkViewModel.setSelectedArtwork(index)
                                                    mainNavController.navigate(Screen.DETAILS.name)
                                                },
                                                onFavoriteButtonClicked = { artwork, isFavorite ->
                                                    artworkViewModel.modifyFavoriteStateOn(
                                                        artwork,
                                                        isFavorite
                                                    )
                                                })
                                        }
                                    })
                            }
                        }
                        composable(Screen.DETAILS.name) {
                            DetailsScreen {
                                mainNavController.navigate(Screen.HOME.name)
                            }
                        }
                    })
                artworkViewModel.userMessage.value?.let {
                    ShowMessage(message = it)
                    artworkViewModel.clearUserMessage()
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val selectedItemIndex = rememberSaveable {
        mutableIntStateOf(0)
    }
    val navigationItems = listOf(
        MenuItem.Discover(),
        MenuItem.AR(),
        MenuItem.Search(),
        MenuItem.Favorites()
    )
    NavigationBar {
        navigationItems.forEachIndexed { index, menuItem ->
            val contentDesc = stringResource(id = menuItem.contentDesc)
            NavigationBarItem(
                selected = index == selectedItemIndex.intValue,
                onClick = {
                    selectedItemIndex.intValue = index
                    navController.navigate(menuItem.screen.name)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(id = R.color.black),
                    unselectedIconColor = colorResource(id = R.color.black_50),
                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        LocalAbsoluteTonalElevation.current // Removes the indicator behind navbar items
                    )
                ),
                modifier = Modifier.semantics {
                    contentDescription = contentDesc
                },
                icon = {
                    Icon(
                        painter = painterResource(id = menuItem.selectedIcon),
                        contentDescription = stringResource(id = menuItem.contentDesc)
                    )
                }
            )
        }
    }
}

@Composable
private fun ShowMessage(@StringRes message: Int) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}