package com.example.rule.di

import com.example.rule.RuleFeature
import com.example.rule.RuleFeatureImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object RuleModule {
    @Provides
    fun provideRuleFeature(): RuleFeature {
        return RuleFeatureImpl()
    }

}