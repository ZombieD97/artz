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
        R.drawable.menu_item_discover_selected,
        Screen.HOME_DISCOVER,
        R.string.menuItem_discover_contendesc
    )

    class AR : MenuItem(
        R.drawable.menu_item_ar_selected,
        Screen.HOME_AR,
        R.string.menuItem_discover_contendesc
    )

    class Search : MenuItem(
        R.drawable.menu_item_search_selected,
        Screen.HOME_SEARCH,
        R.string.menuItem_discover_contendesc
    )

    class Favorites : MenuItem(
        R.drawable.menu_item_favorites_selected,
        Screen.HOME_FAVORITES,
        R.string.menuItem_discover_contendesc
    )
}