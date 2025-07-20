package com.novislide.di

import com.novislide.data.repository.FakeGreetingRepository
import com.novislide.domain.repository.GreetingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindGreetingRepository(
        repo: FakeGreetingRepository
    ): GreetingRepository
}
