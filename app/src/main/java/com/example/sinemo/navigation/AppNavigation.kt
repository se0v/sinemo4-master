package com.example.sinemo.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sinemo.screens.LazyListScreen
import com.example.sinemo.screens.MainScreen

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AppNavigation(
    navController : NavHostController
){

    NavHost(
        navController = navController,
        startDestination = AppScreen.MainScreen.route
    ){
        composable(
            route = AppScreen.LazyList.route
        ){
            LazyListScreen()
        }
        composable(
            route = AppScreen.MainScreen.route
        ){
            MainScreen(navController)
        }
    }

}