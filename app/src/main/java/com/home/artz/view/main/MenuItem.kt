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

    class AR : MenuItem(
        R.drawable.menu_item_ar,
        Screen.HOME_AR,
        R.string.menuItem_discover_contentdesc
    )

    class Search : MenuItem(
        R.drawable.menu_item_search,
        Screen.HOME_SEARCH,
        R.string.menuItem_discover_contentdesc
    )

    class Favorites : MenuItem(
        R.drawable.menu_item_favorites,
        Screen.HOME_FAVORITES,
        R.string.menuItem_discover_contentdesc
    )
}