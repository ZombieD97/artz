package com.home.artz.model.datamodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    @ColumnInfo(name = "_links") @field:Json(name = "_links") val links: Links,
    @ColumnInfo(name = "creators") var creators: List<ArtworkCreators>? = null,
    var isFavorite: Boolean = false
)

data class ArtworkCreators(
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "name") var name: String?
)

data class Links(
    @field:Json(name = "image") val imageLinks: ImageLinks?
)

data class ImageLinks(
    @field:Json(name = "href") val imageUrl: String,
    @ColumnInfo(name = "mediumImage") var mediumImage: String?,
    @ColumnInfo(name = "largeImageUrl") var largeImageUrl: String?,
)

data class ArtworkDimensions(@field:Json(name = "cm") val dimensions: MetricDimensions?)

data class MetricDimensions(@field:Json(name = "text") val size: String?)
