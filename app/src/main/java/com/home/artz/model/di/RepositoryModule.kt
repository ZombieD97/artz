package com.home.artz.model.di

import android.content.Context
import androidx.room.Room
import com.home.artz.model.database.ArtzDatabase
import com.home.artz.model.database.ArtworkTypeConverters
import com.home.artz.model.network.AuthenticationInterceptor
import com.home.artz.model.network.APIService
import com.home.artz.model.repository.ArtworkRepository
import com.home.artz.model.repository.Constants
import com.home.artz.model.repository.IArtworkRepository
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
    fun provideCommunicationService(okHttpClient: OkHttpClient): APIService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                okHttpClient.newBuilder()
                    .addInterceptor(logging)
                    .addInterceptor(AuthenticationInterceptor()).build()
            )
            .build().create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        ArtzDatabase::class.java, Constants.DATABASE_NAME
    ).addTypeConverter(ArtworkTypeConverters()).build()

    @Provides
    fun provideArtworkRepository(
        apiService: APIService,
        database: ArtzDatabase
    ): IArtworkRepository = ArtworkRepository(apiService, database)
}