package com.example.superheroesdemo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.superheroesdemo.Route
import com.example.superheroesdemo.presentation.ui.theme.SuperHeroesComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperHeroesComposeTheme {
                NavigationView()
            }
        }
    }
}

@Composable
fun NavigationView() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.CardLikesScreen.route) {
        composable(route = Route.SearchScreen.route) {
            SearchScreen { route ->
                when (route) {
                    is Route.NavigateBack -> navController.popBackStack()
                    is Route.DetailsScreenArgsValues -> {
                        val carachterId = route.id
                        navController.navigate(Route.DetailsScreenArgsValues(carachterId).route)
                    }
                    is Route.CardLikesScreen -> {
                        navController.navigate(Route.CardLikesScreen.route)
                    }
                    else -> {} //not necessary
                }
            }
        }
        composable(
            route = Route.DetailScreenArgsName("id").route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                },
            )
        ) {
            it.arguments?.getInt("id")?.let { itemId->
                DetailScreen(characterId = itemId, onNavBack = {
                    navController.popBackStack()
                })
            }
        }
        composable(route = Route.CardLikesScreen.route) {
            CardLikesScreen(
                onNavigation = { route->
                    when (route) {
                        is Route.NavigateBack -> navController.popBackStack()
                        is Route.SearchScreen -> navController.navigate(Route.SearchScreen.route)
                        is Route.DetailsScreenArgsValues -> {
                            val carachterId = route.id
                            navController.navigate(Route.DetailsScreenArgsValues(carachterId).route)
                        }
                        else -> {}
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    SuperHeroesComposeTheme {
    }
}
