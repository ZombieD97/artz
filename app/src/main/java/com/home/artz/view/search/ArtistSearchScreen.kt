package com.home.artz.view.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.home.artz.R
import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.SearchResult
import com.home.artz.view.ui.components.ImageLoadingPlaceholder
import com.home.artz.view.ui.components.Loader
import com.home.artz.view.ui.theme.GreenPrimary
import com.home.artz.view.ui.theme.GreenSecondary
import com.home.artz.view.ui.theme.Tertiary

@Composable
fun ArtistSearchScreen(
    searchResults: List<SearchResult>?,
    popularArtists: List<Artist>?,
    contentPadding: PaddingValues,
    searchText: (String) -> Unit,
    onSearchCleared: () -> Unit,
    onSearchResultSelected: (SearchResult) -> Unit,
    onPopularArtistClicked: (Artist) -> Unit,
    onBackClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    ConstraintLayout(
        Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null // Remove ripple effect
            ) {
                focusManager.clearFocus()
            }
            .padding(contentPadding)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        val (inputFieldRef, dropDownListRef, popularArtistsListRef) = createRefs()
        val inputText = remember {
            mutableStateOf<String?>(null)
        }
        val showLoader = remember {
            mutableStateOf(false)
        }
        val inputFieldHasFocus = remember {
            mutableStateOf(false)
        }

        InputField(
            inputFieldRef = inputFieldRef,
            inputText = inputText,
            showLoader = showLoader,
            inputFieldHasFocus = inputFieldHasFocus,
            focusRequester = focusRequester,
            onSearchCleared = onSearchCleared,
            searchText = searchText
        )
        PopularArtistList(
            popularArtistsListRef = popularArtistsListRef,
            inputFieldRef = inputFieldRef,
            popularArtists = popularArtists,
            inputText = inputText,
            onPopularArtistClicked = onPopularArtistClicked,
            onSearchCleared = onSearchCleared
        )
        SearchDropdown(
            dropdownListRef = dropDownListRef,
            inputFieldRef = inputFieldRef,
            focusManager = focusManager,
            searchResults = searchResults,
            showLoader = showLoader,
            inputText = inputText,
            inputFieldHasFocus = inputFieldHasFocus,
            onSearchResultSelected = onSearchResultSelected,
            onBackClicked = onBackClicked,
            onSearchCleared = onSearchCleared
        )
    }
}

@Composable
fun ConstraintLayoutScope.InputField(
    inputFieldRef: ConstrainedLayoutReference,
    inputText: MutableState<String?>,
    showLoader: MutableState<Boolean>,
    inputFieldHasFocus: MutableState<Boolean>,
    focusRequester: FocusRequester,
    onSearchCleared: () -> Unit,
    searchText: (String) -> Unit
) {
    val paddingNormal = dimensionResource(id = R.dimen.padding_normal)

    TextField(
        value = inputText.value ?: "",
        onValueChange = {
            inputText.value = it
            if (it.isBlank()) onSearchCleared.invoke()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Words
        ),
        keyboardActions = KeyboardActions(onSearch = {
            if (!inputText.value.isNullOrBlank()) {
                showLoader.value = true
                searchText.invoke(inputText.value!!)
            }
        }),
        placeholder = { Text(stringResource(id = R.string.search_for_artist)) },
        leadingIcon = {
            if (showLoader.value) {
                Loader()
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.icon_search),
                    contentDescription = stringResource(
                        id = R.string.menuItem_search_contentdesc
                    ),
                    tint = Tertiary,
                    modifier = Modifier
                        .clickable {
                            if (!inputText.value.isNullOrBlank()) {
                                showLoader.value = true
                                searchText.invoke(inputText.value!!)
                            }
                        }
                        .size(dimensionResource(id = R.dimen.favorite_icon_size))
                )
            }
        },
        trailingIcon = {
            inputText.value?.let {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = stringResource(
                        id = R.string.clear_search_content_desc
                    ),
                    tint = Tertiary,
                    modifier = Modifier.clickable {
                        inputText.value = null
                        onSearchCleared.invoke()
                    }
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .border(
                BorderStroke(
                    width = dimensionResource(id = R.dimen.padding_tiny),
                    color = GreenPrimary
                ),
                shape = RoundedCornerShape(50)
            )
            .onFocusChanged {
                inputFieldHasFocus.value = it.hasFocus
            }
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .padding(start = paddingNormal, end = paddingNormal)
            .constrainAs(inputFieldRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
        singleLine = true
    )
}

@Composable
private fun ConstraintLayoutScope.SearchDropdown(
    dropdownListRef: ConstrainedLayoutReference,
    inputFieldRef: ConstrainedLayoutReference,
    focusManager: FocusManager,
    searchResults: List<SearchResult>?,
    showLoader: MutableState<Boolean>,
    inputText: MutableState<String?>,
    inputFieldHasFocus: MutableState<Boolean>,
    onSearchResultSelected: (SearchResult) -> Unit,
    onSearchCleared: () -> Unit,
    onBackClicked: () -> Unit
) {
    val paddingNormal = dimensionResource(id = R.dimen.padding_normal)
    var showDropdown = false
    Column(
        modifier = Modifier
            .background(
                GreenSecondary,
                shape = RoundedCornerShape(
                    bottomEnd = paddingNormal,
                    bottomStart = paddingNormal
                )
            )
            .fillMaxWidth(0.85F)
            .constrainAs(dropdownListRef) {
                top.linkTo(inputFieldRef.bottom)
                start.linkTo(inputFieldRef.start)
                end.linkTo(inputFieldRef.end)
            }
    ) {
        showLoader.value = false
        showDropdown = !inputText.value.isNullOrBlank() && inputFieldHasFocus.value && searchResults != null
        if (showDropdown) {
            if (searchResults!!.isNotEmpty()) {
                LazyColumn(contentPadding = PaddingValues(paddingNormal), content = {
                    itemsIndexed(searchResults) { index, searchResult ->
                        Text(
                            fontSize = dimensionResource(id = R.dimen.search_dropdown_text_size).value.sp,
                            text = searchResult.title,
                            modifier = Modifier.clickable {
                                onSearchResultSelected.invoke(searchResult)
                                inputText.value = null
                                onSearchCleared.invoke()
                            }
                        )
                        if (index != searchResults.lastIndex) {
                            Spacer(modifier = Modifier.height(paddingNormal))
                        }
                    }
                })
            } else {
                Text(
                    modifier = Modifier.padding(paddingNormal),
                    text = stringResource(id = R.string.search_no_result)
                )
            }
        }
    }

    BackHandler {
        if (showDropdown) focusManager.clearFocus() else onBackClicked.invoke()
    }
}

@Composable
private fun ConstraintLayoutScope.PopularArtistList(
    popularArtistsListRef: ConstrainedLayoutReference,
    inputFieldRef: ConstrainedLayoutReference,
    inputText: MutableState<String?>,
    popularArtists: List<Artist>?,
    onSearchCleared: () -> Unit,
    onPopularArtistClicked: (Artist) -> Unit
) {
    val paddingNormal = dimensionResource(id = R.dimen.padding_normal)
    val configuration = LocalConfiguration.current
    val imageSize = configuration.screenWidthDp * 0.45F
    val showLoader = remember {
        mutableStateOf(true)
    }
    Column(
        modifier = Modifier
            .constrainAs(popularArtistsListRef) {
                top.linkTo(inputFieldRef.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
    ) {
        popularArtists?.let { list ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                content = {
                    itemsIndexed(list) { index, artist ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            AsyncImage(
                                model = artist.links?.image?.squareImage,
                                contentDescription = artist.name,
                                placeholder = ImageLoadingPlaceholder(
                                    imageSize,
                                    imageSize,
                                ),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(imageSize.dp)
                                    .clickable {
                                        onPopularArtistClicked(artist)
                                        inputText.value = null
                                        onSearchCleared.invoke()
                                    }
                                    .clip(RoundedCornerShape(paddingNormal))
                            )
                            Text(text = artist.name, textAlign = TextAlign.Center)
                        }
                        if (index == list.lastIndex) showLoader.value = false
                    }
                },
                contentPadding = PaddingValues(bottom = paddingNormal, top = paddingNormal),
                verticalArrangement = Arrangement.spacedBy(paddingNormal),
                horizontalArrangement = Arrangement.spacedBy(paddingNormal)
            )
        }
    }

    if (showLoader.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Loader()
        }
    }
}