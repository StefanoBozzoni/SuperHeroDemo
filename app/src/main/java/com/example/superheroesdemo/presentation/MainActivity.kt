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
import com.example.superheroesdemo.Routes
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
    NavHost(navController = navController, startDestination = Routes.SearchScreen.route) {
        composable(route = Routes.SearchScreen.route) {
            SearchScreen { carachterId ->
                navController.navigate(Routes.DetailsScreenArgsValues(carachterId).route)
            }
        }
        composable(
            route = Routes.DetailScreenArgsName("id").route,
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
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    SuperHeroesComposeTheme {
    }
}
