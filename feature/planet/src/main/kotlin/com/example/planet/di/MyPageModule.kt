package com.example.planet.di

import com.example.planet.navigation.EditPlanetPostFeature
import com.example.planet.navigation.EditPlanetPostFeatureImpl
import com.example.planet.navigation.MakePlanetFeature
import com.example.planet.navigation.MakePlanetFeatureImpl
import com.example.planet.navigation.PlanetFeature
import com.example.planet.navigation.PlanetFeatureImpl
import com.example.planet.navigation.PlanetPostFeature
import com.example.planet.navigation.PlanetPostFeatureImpl
import com.example.planet.navigation.PreviewFeature
import com.example.planet.navigation.PreviewImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object PlanetModule {
    @Provides
    fun provideEditPlanetPostFeature(): EditPlanetPostFeature {
        return EditPlanetPostFeatureImpl()
    }

    @Provides
    fun provideMakePlanetFeatureFeature(): MakePlanetFeature {
        return MakePlanetFeatureImpl()
    }

    @Provides
    fun providePlanetFeature(): PlanetFeature {
        return PlanetFeatureImpl()
    }

    @Provides
    fun providePlanetPostFeature(): PlanetPostFeature {
        return PlanetPostFeatureImpl()
    }

    @Provides
    fun providePreviewFeature(): PreviewFeature {
        return PreviewImpl()
    }
}