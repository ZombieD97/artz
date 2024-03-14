package com.home.artz.model.datamodel

enum class ImageVersion {
    MEDIUM, LARGE, SQUARE
}
fun appendImageVersion(imageUrl: String?, version: ImageVersion) =
    imageUrl?.removeSuffix("{image_version}.jpg") + "${version.name.lowercase()}.jpg"

fun String?.ifNullOrBlank(defaultValue: String): String {
    return if (isNullOrBlank()) defaultValue else this
}
