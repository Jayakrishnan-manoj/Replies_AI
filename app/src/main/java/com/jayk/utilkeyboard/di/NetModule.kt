package com.jayk.utilkeyboard.di

import com.jayk.utilkeyboard.data.APIRepositoryImpl
import com.jayk.utilkeyboard.data.repository.APIRepository
import com.jayk.utilkeyboard.data.services.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesAPIService(retrofit: Retrofit): APIService{
        return retrofit.create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun providesAPIRepository(apiService: APIService) : APIRepository{
        return APIRepositoryImpl(apiService)
    }

}