package com.home.artz

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.home.artz.model.Constants
import com.home.artz.model.database.ArtzDatabase
import com.home.artz.model.database.ArtzTypeConverter
import com.home.artz.model.database.FavoritesDao
import com.home.artz.model.datamodel.Artwork
import com.home.artz.model.datamodel.ArtworkDimensions
import com.home.artz.model.datamodel.ImageLinks
import com.home.artz.model.datamodel.Links
import com.home.artz.model.datamodel.MetricDimensions
import com.home.artz.model.datamodel.UserMessage
import com.home.artz.model.network.APIService
import com.home.artz.model.network.AuthenticationInterceptor
import com.home.artz.model.repository.artwork.ArtworkRepository
import com.home.artz.model.repository.artwork.IArtworkRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(RobolectricTestRunner::class)
class ArtworksUnitTest {
    private lateinit var server: MockWebServer
    private lateinit var artworkRepository: IArtworkRepository
    private lateinit var moshi: Moshi
    private lateinit var database: ArtzDatabase
    private lateinit var favoritesDao: FavoritesDao

    @Before
    fun init() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArtzDatabase::class.java
        ).allowMainThreadQueries().addTypeConverter(ArtzTypeConverter()).build()
        favoritesDao = database.favoritesDao()

        server = MockWebServer()
        server.start(8000)

        moshi = Moshi.Builder().build()

        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(AuthenticationInterceptor())
            .build()
        val service = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url(Constants.BASE_URL).toString())
            .client(okHttpClient)
            .build().create(APIService::class.java)

        artworkRepository = ArtworkRepository(service, database, moshi)
    }

    @Test
    fun testApiSuccessWithPreSavedArtwork() {
        val mockedResponse = MockResponseFileReader("artworkApi/success.json").content
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedResponse)
        )
        runBlocking {
            val mockFavoriteArtwork = createMockArtwork()
            artworkRepository.saveToFavorites(mockFavoriteArtwork)
            val artworks = artworkRepository.fetchArtworks(true)

            assert(artworks?.none { it.links.imageLinks?.imageUrl == null } == true)
            assert(artworks?.all { it.links.imageLinks?.mediumImageUrl != null || it.links.imageLinks?.largeImageUrl != null } == true)
            assert(artworks?.firstOrNull { it.id == mockFavoriteArtwork.id && it.isFavorite } != null)
        }
    }

    @Test
    @Ignore("Endless loop")
    fun testApiFail() {
        val mockedErrorBody = MockResponseFileReader("artworkApi/fail.json").content
        server.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody(mockedErrorBody)
        )
        runBlocking {
            artworkRepository.fetchArtworks(true)
            artworkRepository.userErrorMessage.collect {
                assert((it as UserMessage.ApiErrorMessage).text == "Something went wrong")
            }
        }
    }

    @Test
    fun testFavoriteArtworkActions() {
        val mockArtwork = createMockArtwork()
        runBlocking {
            artworkRepository.saveToFavorites(mockArtwork)
            assert(favoritesDao.getFavorites().first().id == mockArtwork.id)

            artworkRepository.removeFromFavorites(mockArtwork)
            assert(favoritesDao.getFavorites().isEmpty())
        }
    }

    private fun createMockArtwork(): Artwork {
        return Artwork(
            id = "4d8b92eb4eb68a1b2c000968",
            title = "Der Kuss (The Kiss)",
            collectingInstitution = "Ă–sterreichische Galerie Belvedere, Vienna",
            date = "1907-1908",
            dimensions = ArtworkDimensions(MetricDimensions("180.1 Ă— 180.1 cm")),
            iconicity = 319.9334846264162,
            imageRights = "Source: Wikimedia Commons / Public Domain",
            medium = "Oil and gold leaf on canvas",
            saleMessage = "Not for sale",
            links = Links(
                ImageLinks(
                    imageUrl = "https://d32dm0rphc51dk.cloudfront.net/NOpIAwQa-3r51Cg9qXKbfA/{image_version}.jpg",
                    mediumImageUrl = "https://d32dm0rphc51dk.cloudfront.net/NOpIAwQa-3r51Cg9qXKbfA/medium.jpg",
                    largeImageUrl = "https://d32dm0rphc51dk.cloudfront.net/NOpIAwQa-3r51Cg9qXKbfA/large.jpg"
                )
            )
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
        database.close()
    }
}