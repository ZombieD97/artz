package com.home.artz.model.datamodel

import com.squareup.moshi.Json

data class Artist(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "gender") val gender: String? = null,
    @field:Json(name = "biography") val biography: String? = null,
    @field:Json(name = "birthday") val birthday: String? = null,
    @field:Json(name = "deathday") val deathday: String? = null,
    @field:Json(name = "hometown") val hometown: String? = null,
    @field:Json(name = "nationality") val nationality: String? = null,
    @field:Json(name = "_links") val links: ArtistLinks? = null
)

data class ArtistLinks(
    @field:Json(name = "permalink") val moreInfo: ArtistUrl? = null,
    @field:Json(name = "image") val image: ArtistUrl? = null
)

data class ArtistUrl(
    @field:Json(name = "href") val url: String,
    var squareImage: String?
)