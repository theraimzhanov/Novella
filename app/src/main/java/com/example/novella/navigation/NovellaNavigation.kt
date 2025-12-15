package com.example.novella.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.novella.screens.NovellaSplashScreen
import com.example.novella.screens.details.DetailsScreen
import com.example.novella.screens.home.HomeScreen
import com.example.novella.screens.login.LoginScreen
import com.example.novella.screens.search.SearchScreen
import com.example.novella.screens.search.SearchViewModel
import com.example.novella.screens.stats.StatsScreen

@Composable
fun NovellaNavigation(){
    val navController=rememberNavController()
    NavHost(navController=navController, startDestination = NovellaScreens.SplashScreen.name){

        composable(NovellaScreens.SplashScreen.name){
            NovellaSplashScreen(navController=navController)
        }

        composable(NovellaScreens.HomeScreen.name){
            HomeScreen(navController=navController)
        }

        composable(NovellaScreens.LoginScreen.name){
            LoginScreen(navController=navController)
        }

        composable(NovellaScreens.StatsScreen.name){
            StatsScreen(navController=navController)
        }
        composable(NovellaScreens.SearchScreen.name){
            val viewModel= hiltViewModel<SearchViewModel>()
            SearchScreen(navController=navController,viewModel)
        }
val detailName = NovellaScreens.DetailScreen.name

        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type= NavType.StringType
        })){backStackEntry->
            backStackEntry.arguments?.getString("bookId").let {
                DetailsScreen(navController=navController, bookId = it.toString())
            }

        }
    }
}