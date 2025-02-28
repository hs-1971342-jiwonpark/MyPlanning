package navigation

import androidx.annotation.StringRes
import com.example.myplanning.R
import com.example.navigation.Dest
import com.example.navigation.NavigationDest
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
        route = Dest.PlanetRoute::class
    ),
    Main(
        selectedIcon = R.drawable.ic_setting,
        unselectedIcon = R.drawable.ic_setting,
        iconTextId = R.string.mypage,
        titleTextId = R.string.mypage,
        route = Dest.MainRoute::class
    ),
    Login(
        selectedIcon = 0,
        unselectedIcon = 0,
        iconTextId = R.string.login,
        titleTextId = R.string.mypage,
        route = Dest.LoginRoute::class
    ),
    MyPage(
        selectedIcon = R.drawable.ic_setting,
        unselectedIcon = R.drawable.ic_setting,
        iconTextId = R.string.mypage,
        titleTextId = R.string.mypage,
        route = Dest.MyPageRoute::class
    )
}

