package com.home.artz.data.model

import com.squareup.moshi.Json

data class DiscoverEmbeddedResponse(@field:Json(name = "_embedded") val embeddedResponse: DiscoverResponse)
data class DiscoverResponse(@field:Json(name = "artworks") val artworks: List<Artwork>)
