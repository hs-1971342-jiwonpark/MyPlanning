package navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.login.LoginRoute
import com.example.mypage.MyPageRoute
import com.example.myplanning.R
import com.example.planet.navigation.PlanetRoute
import ui.MainRoute
import kotlin.reflect.KClass

enum class NavigationDestination(
    val selectedIcon: Int,
    val unselectedIcon: Int,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>
) {
    Planet(
        selectedIcon = R.drawable.ic_planet,
        unselectedIcon = R.drawable.ic_planet,
        iconTextId = R.string.planet,
        titleTextId = R.string.planet,
        route = PlanetRoute::class
    ),
    Main(
        selectedIcon = R.drawable.ic_setting,
        unselectedIcon = R.drawable.ic_setting,
        iconTextId = R.string.mypage,
        titleTextId = R.string.mypage,
        route = MainRoute::class
    ),
    Login(
        selectedIcon = 0,
        unselectedIcon = 0,
        iconTextId = R.string.login,
        titleTextId = R.string.mypage,
        route = LoginRoute::class
    ),
    MyPage(
        selectedIcon = R.drawable.ic_setting,
        unselectedIcon = R.drawable.ic_setting,
        iconTextId = R.string.mypage,
        titleTextId = R.string.mypage,
        route = MyPageRoute::class
    )
}

