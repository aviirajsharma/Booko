package com.avirajsharma.booko.presentation.navigation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.avirajsharma.booko.presentation.screens.detailscreen.BookDetailScreen
import com.avirajsharma.booko.presentation.screens.homescreen.HomeScreen
import com.avirajsharma.booko.presentation.screens.mybooksscreen.MyBooksScreen
import com.avirajsharma.booko.presentation.screens.searchscreen.SearchScreen
import com.avirajsharma.booko.presentation.screens.settingsscreen.SettingsScreen

@Composable
fun BookoNav(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = bottomBarItems.any { currentDestination?.hasRoute(it.route::class) == true }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomBarItems.forEach { screen ->
                        val isSelected = currentDestination?.hasRoute(screen.route::class) == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) screen.selectedIcon else screen.icon,
                                    contentDescription = screen.label
                                )
                            },
                            label = {
                                Text(screen.label)
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = modifier.padding(innerPadding)
        ) {
            composable<Home> {
                HomeScreen(
                    onBookCardClick = { bookId ->
                        navController.navigate(Detail(bookId))
                    },
                    onSearchClicked = {
                        navController.navigate(Search)
                    }
                )
            }
            composable<Search> {
                SearchScreen(
                    onBookCardClick = { bookId ->
                        navController.navigate(Detail(bookId))
                    }
                )
            }

            composable<Detail> { backStackEntry ->
                val bookId = backStackEntry.toRoute<Detail>().bookId
                val context = LocalContext.current
                BookDetailScreen(bookId = bookId, onReadOnlineClick = {
                    val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                    context.startActivity(intent)
                })
            }

            composable<MyBooks> {
                MyBooksScreen()
            }

            composable<Settings> {
                SettingsScreen()
            }
        }
    }
}

sealed class Screen(
    val route: Any,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object HomeScreen : Screen(
        route = Home,
        label = "Home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )

    data object MyBooksScreen : Screen(
        route = MyBooks,
        label = "My Books",
        icon = Icons.Outlined.Book,
        selectedIcon = Icons.Filled.Book,
    )

    data object SettingsScreen : Screen(
        route = Settings,
        label = "Settings",
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings
    )
}

val bottomBarItems = listOf(
    Screen.HomeScreen,
    Screen.MyBooksScreen,
    Screen.SettingsScreen
)
