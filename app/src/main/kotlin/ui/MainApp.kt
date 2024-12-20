package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.designsystem.theme.main
import com.example.login.LoginRoute
import com.example.myplanning.R
import com.example.planet.navigation.PlanetRoute
import navigation.AppNavHost
import navigation.NavigationDestination
import kotlin.reflect.KClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val snackbarHostState = SnackbarHostState()
    val astate = rememberMainAppState()
    val currentDestination = astate.currentTopLevelDestination
    Scaffold(
        topBar = {
            if (currentDestination != null) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = main // AppBar 배경색을 main으로 설정
                    ),
                    title = {
                        Image(
                            contentScale = ContentScale.Fit,
                            painter = painterResource(
                                id = R.drawable.ic_title_logo
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(120.dp)
                                .padding(start = 8.dp) // 좌우 여백 추가
                        )
                    },
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                appState = astate,
                onItemSelected = { destination ->
                    astate.navigateToTopLevelDestination(destination)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavHost(
                appState = astate,
                startDestination = PlanetRoute::class,
                context = LocalContext.current
            )
        }
    }
}


@Composable
fun BottomNavigationBar(
    appState: MainAppState,
    onItemSelected: (NavigationDestination) -> Unit
) {
    val currentDestination = appState.currentDestination
    NavigationBar(
        containerColor = main,
    ) {
        appState.navigationDestinations
            .filterNot { it.route == LoginRoute::class || it.route == MainRoute::class}
            .forEach { destination ->
                val selected = currentDestination
                    .isRouteInHierarchy(destination.route)

                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemSelected(destination) },
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = if (selected) destination.selectedIcon else destination.unselectedIcon
                            ),
                            contentDescription = stringResource(id = destination.iconTextId)
                        )
                    },
                    label = {
                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = stringResource(
                                id = destination.iconTextId,
                            ),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White, // 선택된 아이콘 색상
                        unselectedIconColor = Color.Gray, // 비선택 아이콘 색상
                        selectedTextColor = Color.White, // 선택된 텍스트 색상
                        unselectedTextColor = Color.Gray, // 비선택 텍스트 색상
                        indicatorColor = Color.Transparent // 선택된 항목 배경을 투명하게 설정
                    )
                )
            }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false