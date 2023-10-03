package com.example.superheroesdemo

sealed class Routes(val route: String) {
    object SearchScreen: Routes("SearchScreen")
    object DetailScreen: Routes("DetailScreen")
    class  DetailScreenArgsName(arg1: String): Routes("${DetailScreen.route}/{$arg1}")
    class  DetailsScreenArgsValues(id: Int): Routes("${DetailScreen.route}/$id")
}