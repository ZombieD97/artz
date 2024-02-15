package com.home.artz.data.model

import com.squareup.moshi.Json

data class Artwork(@field:Json(name = "id") val id: String, @field:Json(name = "slug") val slug: String)
