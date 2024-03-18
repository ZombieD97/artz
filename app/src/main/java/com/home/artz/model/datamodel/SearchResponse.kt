package com.home.artz.model.datamodel

import com.squareup.moshi.Json

data class SearchEmbeddedResponse(
    @field:Json(name = "_embedded") val embeddedResponse: SearchResponse
)

data class SearchResponse(@field:Json(name = "results") val results: List<SearchResult>)

data class SearchResult(
    @field:Json(name = "type") val type: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "_links") val searchLink: SearchLink
)

data class SearchLink(
    @field:Json(name = "self") val link: SearchUrl
)

data class SearchUrl(
    @field:Json(name = "href") val url: String
)