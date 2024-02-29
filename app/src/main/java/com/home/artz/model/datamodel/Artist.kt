package com.home.artz.model.datamodel

import com.squareup.moshi.Json

data class Artist(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String)