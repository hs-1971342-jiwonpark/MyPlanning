package di

import com.example.login.LoginFeature
import com.example.myaccount.MyAccountFeature
import com.example.mypage.navigation.HoldPlanetFeature
import com.example.mypage.navigation.MyPageFeature
import com.example.planet.navigation.MakePlanetFeature
import com.example.planet.navigation.PlanetFeature
import com.example.planet.navigation.PlanetPostFeature
import com.example.planet.navigation.PreviewFeature
import com.example.rule.RuleFeature
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ui.MainFeature
import ui.MainFeatureImpl


@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideDefaultNavigation(
        logInFeature: LoginFeature,
        mainFeature: MainFeature,
        planetFeature: PlanetFeature,
        previewFeature: PreviewFeature,
        myPageFeature: MyPageFeature,
        holdPlanetFeature: HoldPlanetFeature,
        ruleFeature: RuleFeature,
        myAccountFeature: MyAccountFeature,
        makePlanetFeature: MakePlanetFeature,
        planetPostFeature: PlanetPostFeature
    ): DefaultNavigator {
        return DefaultNavigator(
            logInFeature,
            mainFeature,
            planetFeature,
            previewFeature,
            myPageFeature,
            holdPlanetFeature,
            ruleFeature,
            myAccountFeature,
            makePlanetFeature,
            planetPostFeature
        )
    }

    @Provides
    fun provideMainFeature() : MainFeature {
        return MainFeatureImpl()
    }
}