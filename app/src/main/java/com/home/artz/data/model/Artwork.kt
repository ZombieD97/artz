package com.home.artz.data.model

import com.squareup.moshi.Json

data class Artwork(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "collecting_institution") val collectingInstitution: String,
    @field:Json(name = "additional_information") val additionalInfo: String,
    @field:Json(name = "iconicity") val iconicity: Double,
    @field:Json(name = "sale_message") val saleMessage: String,
    @field:Json(name = "dimensions") val dimensions: ArtworkDimensions,
    @field:Json(name = "medium") val contentDescription: String,
    @field:Json(name = "image_versions") val imageSizeVariations: List<String>,
    @field:Json(name = "_links") val imageLinks: ImageLinks,
    var isFavorite: Boolean = false
)

data class ImageLinks(@field:Json(name = "image") val artworkUrl: ArtworkUrl)
data class ArtworkUrl(@field:Json(name = "href") var href: String)
data class ArtworkDimensions(@field:Json(name = "cm") val dimensions: Dimensions)
data class Dimensions(@field:Json(name = "text") val hxw: String)
