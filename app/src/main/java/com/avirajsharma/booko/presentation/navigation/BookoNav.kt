package com.avirajsharma.booko.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.avirajsharma.booko.presentation.screens.detailscreen.BookDetailScreen
import com.avirajsharma.booko.presentation.screens.homescreen.HomeScreen
import com.avirajsharma.booko.presentation.screens.homescreen.HomeScreenViewModel
import com.avirajsharma.booko.presentation.screens.searchscreen.SearchScreen
import com.avirajsharma.booko.presentation.screens.searchscreen.SearchScreenViewModel

@Composable
fun BookoNav(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = Home, modifier = modifier) {
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
            BookDetailScreen(bookId = bookId)
        }
    }

}