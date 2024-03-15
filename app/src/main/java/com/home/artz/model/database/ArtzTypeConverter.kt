package com.home.artz.model.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.home.artz.model.datamodel.ArtworkCreators
import com.home.artz.model.datamodel.ArtworkDimensions
import com.home.artz.model.datamodel.ImageLinks
import com.home.artz.model.datamodel.Links
import com.home.artz.model.datamodel.MetricDimensions

@ProvidedTypeConverter
class ArtzTypeConverter {
    @TypeConverter
    fun imageLinksToString(links: Links?): String? {
        return links?.imageLinks?.let {
            "${it.imageUrl}|${it.mediumImage}|${it.largeImageUrl}"
        }
    }

    @TypeConverter
    fun stringToImageLinks(imageLinksString: String?): Links? {
        return imageLinksString?.let {
            val imageLinksStringSplitList = imageLinksString.split("|")
            Links(
                ImageLinks(
                    imageLinksStringSplitList.first(),
                    imageLinksStringSplitList[1],
                    imageLinksStringSplitList.last()
                )
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
    fun creatorsToString(creators: List<ArtworkCreators>?): String? {
        var string = ""
        creators?.forEachIndexed { index, creator ->
            string += "${creator.id},${creator.name}"
            if (index != creators.lastIndex) string += "|"
        }
        return string.ifEmpty { null }
    }

    @TypeConverter
    fun stringToCreators(creatorsString: String?): List<ArtworkCreators>? {
        return if (!creatorsString.isNullOrEmpty()) {
            val creators = arrayListOf<ArtworkCreators>()
            val creatorsStringArray = creatorsString.split("|")
            creatorsStringArray.forEach {
                val creatorStringArray = it.split(",")
                creators.add(ArtworkCreators(creatorStringArray.first(), creatorStringArray.last()))
            }
            creators
        } else {
            null
        }
    }
}