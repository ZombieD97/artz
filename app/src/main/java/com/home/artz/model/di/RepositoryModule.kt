package com.home.artz.model.di

import android.content.Context
import androidx.room.Room
import com.home.artz.model.database.ArtzDatabase
import com.home.artz.model.database.ArtzTypeConverter
import com.home.artz.model.network.APIService
import com.home.artz.model.network.AuthenticationInterceptor
import com.home.artz.model.Constants
import com.home.artz.model.repository.artist.ArtistRepository
import com.home.artz.model.repository.artist.IArtistRepository
import com.home.artz.model.repository.artwork.ArtworkRepository
import com.home.artz.model.repository.artwork.IArtworkRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    fun provideCommunicationService(okHttpClient: OkHttpClient, moshi: Moshi): APIService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(
                okHttpClient.newBuilder()
                    .addInterceptor(logging)
                    .addInterceptor(AuthenticationInterceptor()).build()
            )
            .build().create(APIService::class.java)
    }

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        ArtzDatabase::class.java, Constants.DATABASE_NAME
    ).addTypeConverter(ArtzTypeConverter()).build()

    @Provides
    fun provideArtworkRepository(
        apiService: APIService,
        database: ArtzDatabase,
        moshi: Moshi
    ): IArtworkRepository = ArtworkRepository(apiService, database, moshi)

    @Provides
    fun provideArtistRepository(
        apiService: APIService,
        moshi: Moshi
    ): IArtistRepository = ArtistRepository(apiService, moshi)
}