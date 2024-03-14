package com.home.artz.model.datamodel

import com.squareup.moshi.Json

data class ApiError(
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "message") val message: String?,
)
