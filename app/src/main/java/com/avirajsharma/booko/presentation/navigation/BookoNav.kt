package com.avirajsharma.booko.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avirajsharma.booko.presentation.screens.homescreen.HomeScreen
import com.avirajsharma.booko.presentation.screens.homescreen.HomeScreenViewModel
import com.avirajsharma.booko.presentation.screens.searchscreen.SearchScreen
import com.avirajsharma.booko.presentation.screens.searchscreen.SearchScreenViewModel

@Composable
fun BookoNav(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
    searchScreenViewModel: SearchScreenViewModel
) {

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = Home, modifier = modifier) {
        composable<Home> {
            HomeScreen(
                onSearchClicked = {
                    navController.navigate(Search)
                },
                viewModel = homeScreenViewModel
            )
        }
        composable<Search> {
            SearchScreen(
                viewModel = searchScreenViewModel
            )
        }
    }

}