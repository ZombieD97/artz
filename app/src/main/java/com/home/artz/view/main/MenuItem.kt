package com.home.artz.view.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.home.artz.R

sealed class MenuItem(
    @DrawableRes var selectedIcon: Int,
    var screen: Screen,
    @StringRes var contentDesc: Int
) {
    class Discover : MenuItem(
        R.drawable.menu_item_discover,
        Screen.HOME_DISCOVER,
        R.string.menuItem_discover_contentdesc
    )

    class ArtistSearch : MenuItem(
        R.drawable.menu_item_artist_search,
        Screen.HOME_SEARCH_ARTISTS,
        R.string.menuItem_search_contentdesc
    )

    class Favorites : MenuItem(
        R.drawable.menu_item_favorites,
        Screen.HOME_FAVORITES,
        R.string.menuItem_favorites_contentdesc
    )
}