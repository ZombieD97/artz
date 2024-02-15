package com.home.artz.di

import com.home.artz.data.communication.AuthenticationInterceptor
import com.home.artz.data.communication.CommunicationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    private const val BASE_URL = "https://api.artsy.net/"

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    fun provideCommunicationService(okHttpClient: OkHttpClient): CommunicationService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient.newBuilder()
                .addInterceptor(logging)
                .addInterceptor(AuthenticationInterceptor()).build())
            .build().create(CommunicationService::class.java)
    }
}