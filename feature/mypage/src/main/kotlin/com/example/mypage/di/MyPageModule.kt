package com.example.mypage.di

import com.example.mypage.navigation.HoldPlanetFeature
import com.example.mypage.navigation.HoldPlanetFeatureImpl
import com.example.mypage.navigation.MyPageFeature
import com.example.mypage.navigation.MyPageFeatureImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object MyPageModule {
    @Provides
    fun provideMyPageFeature(): MyPageFeature {
        return MyPageFeatureImpl()
    }

    @Provides
    fun provideHoldPlanetFeature(): HoldPlanetFeature {
        return HoldPlanetFeatureImpl()
    }
}