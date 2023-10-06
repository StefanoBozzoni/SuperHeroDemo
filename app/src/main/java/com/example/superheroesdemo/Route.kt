package com.example.superheroesdemo

sealed class Route(val route: String) {
    object NavigateBack: Route("NavigateBack")
    object SearchScreen: Route("SearchScreen")
    object DetailScreen: Route("DetailScreen")
    class  DetailScreenArgsName(arg1: String): Route("${DetailScreen.route}/{$arg1}")
    class  DetailsScreenArgsValues(val id: Int): Route("${DetailScreen.route}/$id")
    object CardLikesScreen: Route("CardLikesScreen")
}