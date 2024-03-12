package com.home.artz.model.datamodel

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.home.artz.R
import com.squareup.moshi.Json

@Entity("FavoriteArtworks")
data class Artwork(
    @PrimaryKey @field:Json(name = "id") val id: String,
    @ColumnInfo(name = "title") @field:Json(name = "title") val title: String,
    @ColumnInfo(name = "collecting_institution") @field:Json(name = "collecting_institution") val collectingInstitution: String?,
    @ColumnInfo(name = "image_rights") @field:Json(name = "image_rights") val imageRights: String?,
    @ColumnInfo(name = "date") @field:Json(name = "date") val date: String?,
    @ColumnInfo(name = "medium") @field:Json(name = "medium") val medium: String?,
    @ColumnInfo(name = "iconicity") @field:Json(name = "iconicity") val iconicity: Double,
    @ColumnInfo(name = "sale_message") @field:Json(name = "sale_message") val saleMessage: String?,
    @ColumnInfo(name = "dimensions") @field:Json(name = "dimensions") val dimensions: ArtworkDimensions?,
    @ColumnInfo(name = "_links") @field:Json(name = "_links") val imageLinks: ImageLinks,
    @ColumnInfo(name = "artists") var artists: List<Artist>? = null,
    var isFavorite: Boolean = false
)

data class ImageLinks(
    @field:Json(name = "image") val artworkUrl: ArtworkUrl?,
    @field:Json(name = "similar_artworks") val similarArtworks: ArtworkUrl,
)

data class ArtworkUrl(
    @field:Json(name = "href") val imageUrl: String,
    var mediumImage: String?,
    var largeImageUrl: String?,
)

data class ArtworkDimensions(@field:Json(name = "cm") val dimensions: MetricDimensions?)

data class MetricDimensions(@field:Json(name = "text") val size: String?)
