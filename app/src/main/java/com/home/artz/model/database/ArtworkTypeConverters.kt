package com.home.artz.model.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.home.artz.model.datamodel.ArtworkDimensions
import com.home.artz.model.datamodel.ArtworkUrl
import com.home.artz.model.datamodel.ImageLinks
import com.home.artz.model.datamodel.MetricDimensions

@ProvidedTypeConverter
class ArtworkTypeConverters {
    @TypeConverter
    fun imageLinksToString(imageLinks: ImageLinks?): String? {
        return imageLinks?.let {
            "${it.artworkUrl.href}|${it.similarArtworks.href}"
        }
    }

    @TypeConverter
    fun stringToImageLinks(imageLinksString: String?): ImageLinks? {
        return imageLinksString?.let {
            val imageLinksStringSplitList = imageLinksString.split("|")
            ImageLinks(
                ArtworkUrl(imageLinksStringSplitList.first()),
                ArtworkUrl(imageLinksStringSplitList.last())
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
}