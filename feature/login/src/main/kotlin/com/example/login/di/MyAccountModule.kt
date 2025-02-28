package com.example.login.di

import com.example.login.LoginFeature
import com.example.login.LoginFeatureImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object LoginModule {
    @Provides
    fun provideLoginFeature(): LoginFeature {
        return LoginFeatureImpl()
    }
}