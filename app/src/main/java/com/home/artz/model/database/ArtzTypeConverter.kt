package com.home.artz.model.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.home.artz.model.datamodel.Artist
import com.home.artz.model.datamodel.ArtworkDimensions
import com.home.artz.model.datamodel.ArtworkUrl
import com.home.artz.model.datamodel.ImageLinks
import com.home.artz.model.datamodel.MetricDimensions

@ProvidedTypeConverter
class ArtzTypeConverter {
    @TypeConverter
    fun imageLinksToString(imageLinks: ImageLinks?): String? {
        return imageLinks?.artworkUrl?.let {
            "${it.imageUrl}|${it.imageUrl}"
        }
    }

    @TypeConverter
    fun stringToImageLinks(imageLinksString: String?): ImageLinks? {
        return imageLinksString?.let {
            val imageLinksStringSplitList = imageLinksString.split("|")
            ImageLinks(
                ArtworkUrl(imageLinksStringSplitList.first(), null, null),
                ArtworkUrl(imageLinksStringSplitList.last(), null, null)
            )
        }
    }

    @TypeConverter
    fun artworkDimensionsToString(artworkDimensions: ArtworkDimensions?): String? {
        return artworkDimensions?.dimensions?.size
    }

    @TypeConverter
    fun stringToArtworkDimensions(artworkDimensionsString: String?): ArtworkDimensions? {
        return artworkDimensionsString?.let {
            val metricDimensions = MetricDimensions(it)
            ArtworkDimensions(metricDimensions)
        }
    }

    @TypeConverter
    fun artistsToString(artists: List<Artist>?): String? {
        var string = ""
        artists?.forEachIndexed { index, artist ->
            string += "${artist.id},${artist.name}"
            if (index != artists.lastIndex) string += "|"
        }
        return string.ifEmpty { null }
    }

    @TypeConverter
    fun stringToArtists(artistsString: String?): List<Artist>? {
        return if (!artistsString.isNullOrEmpty()) {
            val artists = arrayListOf<Artist>()
            val artistsStringArray = artistsString.split("|")
            artistsStringArray.forEach {
                val artistStringArray = it.split(",")
                artists.add(Artist(artistStringArray.first(), artistStringArray.last()))
            }
            artists
        } else {
            null
        }
    }
}