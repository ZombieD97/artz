package com.home.artz.model.datamodel

import com.squareup.moshi.Json

data class ArtistsEmbeddedResponse(
    @field:Json(name = "_embedded") val embeddedResponse: ArtistsResponse)

data class ArtistsResponse(@field:Json(name = "artists") val artists: List<Artist>)