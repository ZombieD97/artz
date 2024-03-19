package com.home.artz.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.home.artz.R
import com.home.artz.view.artistdetails.ArtistDetailsScreen
import com.home.artz.view.artworkdetails.ArtworkDetailsScreen
import com.home.artz.view.discover.DiscoverScreen
import com.home.artz.view.favorite.FavoriteScreen
import com.home.artz.view.search.ArtistSearchScreen
import com.home.artz.view.ui.components.UserMessage
import com.home.artz.view.ui.theme.ArtzTheme
import com.home.artz.viewmodel.ArtistViewModel
import com.home.artz.viewmodel.ArtworkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val artworkViewModel: ArtworkViewModel by viewModels()
    private val artistsViewModel: ArtistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        actionBar?.hide()
        setContent {
            ArtzTheme {
                val mainNavController = rememberNavController()
                val homeScreenNavController = rememberNavController()
                NavHost(
                    navController = mainNavController,
                    graph = mainNavController.createGraph(Screen.HOME.name, null) {
                        composable(Screen.HOME.name) {
                            Scaffold(
                                bottomBar = {
                                    BottomNavigationBar(homeScreenNavController)
                                },
                                contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
                            ) { scaffoldPadding ->
                                val artworks = remember {
                                    artworkViewModel.cachedArtworks
                                }
                                val paddingNormal = dimensionResource(id = R.dimen.padding_normal)
                                val contentPadding = PaddingValues(
                                    start = paddingNormal,
                                    end = paddingNormal,
                                    top = paddingNormal,
                                    bottom = scaffoldPadding.calculateBottomPadding()
                                )
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
                                                    mainNavController.navigate(Screen.ARTWORK_DETAILS.name)
                                                },
                                                onFavoriteButtonClicked = { artwork, isFavorite ->
                                                    artworkViewModel.modifyFavoriteStateOn(
                                                        artwork,
                                                        isFavorite
                                                    )
                                                },
                                                onScrollEnded = {
                                                    artworkViewModel.loadMoreArtworks()
                                                }
                                            )
                                        }
                                        composable(Screen.HOME_SEARCH_ARTISTS.name) {
                                            ArtistSearchScreen(
                                                artistsViewModel.searchResults.value,
                                                artistsViewModel.popularArtists.value,
                                                contentPadding,
                                                searchText = { text ->
                                                    artistsViewModel.searchArtists(text)
                                                },
                                                onSearchCleared = {
                                                    artistsViewModel.clearSearch()
                                                },
                                                onSearchResultSelected = { searchResult ->
                                                    artistsViewModel.searchResultSelected(
                                                        searchResult
                                                    )
                                                    mainNavController.navigate(Screen.ARTIST_DETAILS.name)
                                                },
                                                onPopularArtistClicked = {
                                                    artistsViewModel.selectedArtist.value = it
                                                    mainNavController.navigate(Screen.ARTIST_DETAILS.name)
                                                },
                                                onBackClicked = {
                                                    homeScreenNavController.navigate(Screen.HOME_DISCOVER.name) {
                                                        popUpTo(homeScreenNavController.graph.findStartDestination().id)
                                                        launchSingleTop = true
                                                    }
                                                }
                                            )
                                        }
                                        composable(Screen.HOME_FAVORITES.name) {
                                            FavoriteScreen(artworkViewModel.cachedFavoriteArtworks.value,
                                                contentPadding,
                                                { index ->
                                                    artworkViewModel.setSelectedArtwork(index)
                                                    mainNavController.navigate(Screen.ARTWORK_DETAILS.name)
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
                        composable(Screen.ARTWORK_DETAILS.name) {
                            artworkViewModel.selectedArtwork.value?.let { artwork ->
                                ArtworkDetailsScreen(
                                    artwork,
                                    artworkViewModel.selectedArtworkLargeImage,
                                    onBackClicked = {
                                        mainNavController.navigateUp()
                                    },
                                    onFavoriteButtonClicked = { isFavorite ->
                                        artworkViewModel.modifyFavoriteStateOn(
                                            artwork,
                                            isFavorite
                                        )
                                    }
                                )
                            }
                        }
                        composable(Screen.ARTIST_DETAILS.name) {
                            artistsViewModel.selectedArtist.value?.let { artist ->
                                ArtistDetailsScreen(artist) {
                                    mainNavController.navigateUp()
                                }
                            }
                        }
                    })

                artworkViewModel.userMessage.value?.let {
                    UserMessage(message = it)
                    artworkViewModel.clearUserMessage()
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navigationItems = listOf(
        MenuItem.Discover(),
        MenuItem.ArtistSearch(),
        MenuItem.Favorites()
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        navigationItems.forEach { menuItem ->
            val contentDesc = stringResource(id = menuItem.contentDesc)
            val navBackStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry.value?.destination?.route

            NavigationBarItem(
                selected = currentRoute == menuItem.screen.name,
                onClick = {
                    navController.navigate(menuItem.screen.name) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.semantics {
                    contentDescription = contentDesc
                },
                icon = {
                    Icon(
                        painter = painterResource(id = menuItem.icon),
                        contentDescription = stringResource(id = menuItem.contentDesc)
                    )
                }
            )
        }
    }
}