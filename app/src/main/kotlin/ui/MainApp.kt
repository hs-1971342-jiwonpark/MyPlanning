package ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.example.designsystem.theme.SubMain
import com.example.designsystem.theme.main
import com.example.mypage.MyPageRoute
import com.example.myplanning.R
import com.example.planet.navigation.PlanetRoute
import com.example.planet.navigation.navigateToMakePlanet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import navigation.AppNavHost
import navigation.NavigationDestination
import kotlin.reflect.KClass

@Composable
internal fun MainApp(appState: MainAppState) {
    val snackbarHostState = SnackbarHostState()
    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val isLogin by appState.loginViewModel.isLoggedIn.collectAsState()
    val isReady by appState.loginViewModel.isReady.collectAsState()
    val isSuccess by appState.makePlanetViewModel.isSuccess.collectAsState()
    val mainCoroutine = rememberCoroutineScope()
    val currentDestination = navBackStackEntry?.destination?.route


    LaunchedEffect(isLogin, isReady, isSuccess) {
        mainCoroutine.launch {
            delay(1000)
            if (isLogin) {
                appState.loginViewModel.updateIsReady(true)
            }
            if (isSuccess) {
                appState.makePlanetViewModel.updateSuccessState(false)
            }
            appState.planetViewModel.refresh()
        }
    }

    MainScreen(
        snackbarHostState = snackbarHostState,
        currentRoute = currentDestination,
        appState = appState,
        isLogin = isReady,
        onClick = {
            val topLevelNavOptions = navOptions {
                popUpTo(appState.navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            appState.navController.navigateToMakePlanet(topLevelNavOptions)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    snackbarHostState: SnackbarHostState,
    currentRoute: String?,
    appState: MainAppState,
    isLogin: Boolean,
    onClick: () -> Unit
) {
    val shouldDisplayBottomBar = when (currentRoute) {
        PlanetRoute::class.qualifiedName, MyPageRoute::class.qualifiedName -> true
        else -> false
    } && isLogin

    Scaffold(
        containerColor = main,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = main
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
                            .padding(start = 8.dp)
                    )
                },
            )
        },
        bottomBar = {
            if (shouldDisplayBottomBar) {
                BottomNavigationBar(
                    appState = appState,
                    onItemSelected = { destination ->
                        appState.navigateToTopLevelDestination(destination)
                    }
                )
            }

        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            if (shouldDisplayBottomBar) {
                AddPostFloatingButton(
                    onClick = {
                        onClick()
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Log.d("순서", "박스")
            AppNavHost(
                appState = appState
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
        containerColor = SubMain,
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            )
    ) {
        appState.navigationDestinations
            .filter {
                it.route == PlanetRoute::class || it.route == MyPageRoute::class
            }
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

@Composable
fun AddPostFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        containerColor = SubMain,
        contentColor = Color.White
    ) {
        Icon(Icons.Filled.Add, "Floating action button.")
    }
}
