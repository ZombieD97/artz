package com.home.artz

import com.home.artz.model.datamodel.ImageVersion
import com.home.artz.model.datamodel.appendImageVersion
import com.home.artz.model.datamodel.ifNullOrBlank
import org.junit.Test

import org.junit.Assert.*

class DataTransformationUnitTest {
    @Test
    fun testImageVersionAppend() {
        val imageUrl = "https://d32dm0rphc51dk.cloudfront.net/dTGcd0Xx0aEp_MDFdHIUIw/{image_version}.jpg"

        val largeImageUrl = "https://d32dm0rphc51dk.cloudfront.net/dTGcd0Xx0aEp_MDFdHIUIw/large.jpg"
        val largeImageUrlAppend = appendImageVersion(imageUrl, ImageVersion.LARGE)
        assertEquals(largeImageUrl, largeImageUrlAppend)

        val mediumImageUrl = "https://d32dm0rphc51dk.cloudfront.net/dTGcd0Xx0aEp_MDFdHIUIw/medium.jpg"
        val mediumImageUrlAppend = appendImageVersion(imageUrl, ImageVersion.MEDIUM)
        assertEquals(mediumImageUrl, mediumImageUrlAppend)

        val squareImageUrl = "https://d32dm0rphc51dk.cloudfront.net/dTGcd0Xx0aEp_MDFdHIUIw/square.jpg"
        val squareImageUrlAppend = appendImageVersion(imageUrl, ImageVersion.SQUARE)
        assertEquals(squareImageUrl, squareImageUrlAppend)
    }
    @Test
    fun testNullOrBlankWithDefault() {
        val defaultValue = "defaultValue"

        val nullText: String? = null
        assertEquals(nullText.ifNullOrBlank(defaultValue), defaultValue)

        val text = "some text"
        assertEquals(text.ifNullOrBlank(defaultValue), text)
    }
}