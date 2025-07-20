package com.novislide.di

import com.novislide.data.NoviAPI
import com.novislide.data.repository.PlaylistRepository
import com.novislide.data.repository.PlaylistRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindMediaRepository(
        impl: PlaylistRepositoryImpl
    ): PlaylistRepository

    companion object {
        private const val NOVI_BASE_URL = "https://test.onsignage.com/"

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(NOVI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): NoviAPI {
            return retrofit.create(NoviAPI::class.java)
        }
    }
}
