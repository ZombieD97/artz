package com.home.artz.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
import com.home.artz.data.model.Artwork
import com.home.artz.ui.theme.ArtzTheme
import com.home.artz.view.ar.ARScreen
import com.home.artz.view.details.DetailsScreen
import com.home.artz.view.discover.DiscoverScreen
import com.home.artz.view.favorite.FavoriteScreen
import com.home.artz.view.search.SearchScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val artworkViewModel: ArtworkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtzTheme {
                val mainNavController = rememberNavController()
                NavHost(navController = mainNavController, graph = mainNavController.createGraph(Screen.HOME.name, null) {
                    composable(Screen.HOME.name) {
                        val homeScreenNavController = rememberNavController()
                        Scaffold(bottomBar = {
                            BottomNavigationBar(homeScreenNavController)
                        }) { contentPadding ->
                            NavHost(navController = homeScreenNavController, graph = homeScreenNavController.createGraph(Screen.HOME_DISCOVER.name, null) {
                                composable(Screen.HOME_DISCOVER.name) {
                                    DiscoverScreen(artworkViewModel.artworks.value, contentPadding, { artwork ->
                                        artworkViewModel.selectedArtwork.value = artwork
                                        mainNavController.navigate(Screen.DETAILS.name)
                                    }, onFavoriteClicked = { artwork, isFavorite ->
                                        artworkViewModel.modifyFavoriteStateOn(artwork, isFavorite)
                                    })
                                }
                                composable(Screen.HOME_AR.name) {
                                    ARScreen()
                                }
                                composable(Screen.HOME_SEARCH.name) {
                                    SearchScreen()
                                }
                                composable(Screen.HOME_FAVORITES.name) {
                                    FavoriteScreen()
                                }
                            })
                        }
                    }
                    composable(Screen.DETAILS.name) {
                        DetailsScreen {
                            mainNavController.navigate("home")
                        }
                    }
                })
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
                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(LocalAbsoluteTonalElevation.current)
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