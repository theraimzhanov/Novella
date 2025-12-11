package com.example.novella.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.novella.screens.NovellaSplashScreen
import com.example.novella.screens.home.HomeScreen
import com.example.novella.screens.login.LoginScreen

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
    }
}