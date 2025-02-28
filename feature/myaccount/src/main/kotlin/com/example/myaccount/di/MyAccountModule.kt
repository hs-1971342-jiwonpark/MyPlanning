package com.example.myaccount.di

import com.example.myaccount.MyAccountFeature
import com.example.myaccount.MyAccountFeatureImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object MyAccountModule {
    @Provides
    fun provideMyPageFeature(): MyAccountFeature {
        return MyAccountFeatureImpl()
    }
}