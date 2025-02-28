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
import ui.MainAppState
import ui.MainFeature

data class DefaultNavigator(
    val logInFeature : LoginFeature,
    val mainFeature : MainFeature,
    val planetFeature : PlanetFeature,
    val previewFeature : PreviewFeature,
    val myPageFeature : MyPageFeature,
    val holdPlanetFeature: HoldPlanetFeature,
    val ruleFeature: RuleFeature,
    val myAccountFeature : MyAccountFeature,
    val makePlanetFeature : MakePlanetFeature,
    val planetPostFeature : PlanetPostFeature
)