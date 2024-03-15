package com.home.artz.model.datamodel

import com.squareup.moshi.Json

data class ArtworksEmbeddedResponse(
    @field:Json(name = "_embedded") val embeddedResponse: ArtworksResponse,
    @field:Json(name = "_links") val paginationLinks: ArtworksPaginationLinks
)

data class ArtworksPaginationLinks(
    @field:Json(name = "next") val nextPage: ImageLinks,
    @field:Json(name = "self") val currentPage: ImageLinks
)

data class ArtworksResponse(@field:Json(name = "artworks") val artworks: List<Artwork>)
