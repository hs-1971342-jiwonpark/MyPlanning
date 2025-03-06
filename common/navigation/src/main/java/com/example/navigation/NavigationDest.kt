package com.example.navigation

import com.example.data.model.MenuType
import com.example.data.model.PostType
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationDest {

    @Serializable
    data object MainRoute : NavigationDest()

    @Serializable
    data object LoginRoute : NavigationDest()

    @Serializable
    data object MyPageRoute : NavigationDest()

    @Serializable
    data object PlanetRoute : NavigationDest()

    @Serializable
    data object MyAccountRoute : NavigationDest()

    @Serializable
    data object EditPlanetPostRoute : NavigationDest()

    @Serializable
    data object MakePlanetNavigation : NavigationDest()

    @Serializable
    data class PlanetPostRoute(
        val cid : String = ""
    ) : NavigationDest()

    @Serializable
    data class PreviewRoute(
        val initialCardId: String = "",
        val initialPostType: PostType = PostType.NOT
    ) : NavigationDest()

    @Serializable
    data object RuleRoute : NavigationDest()

    @Serializable
    data class HoldPlanetRoute(
        val type : MenuType = MenuType.HOLD
    ): NavigationDest()

}

@Serializable
sealed class Dest {

    @Serializable
    data object MainRoute : Dest()

    @Serializable
    data object LoginRoute : Dest()

    @Serializable
    data object MyPageRoute : Dest()

    @Serializable
    data object PlanetRoute : Dest()

    @Serializable
    data object MyAccountRoute : Dest()

    @Serializable
    data object EditPlanetPostRoute : Dest()

    @Serializable
    data object MakePlanetNavigation : Dest()

    @Serializable
    data class PlanetPostRoute(
        val cid: String = ""
    ) : Dest()

    @Serializable
    data class PreviewRoute(
        val initialCardId: String = "",
        val initialPostType: PostType = PostType.NOT
    ) : Dest()

    @Serializable
    data object RuleRoute : Dest()

    @Serializable
    data class HoldPlanetRoute(
        val type : MenuType = MenuType.HOLD
    ) : Dest()
}