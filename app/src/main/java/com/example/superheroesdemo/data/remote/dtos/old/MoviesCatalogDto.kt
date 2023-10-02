package com.example.superheroesdemo.data.remote.dtos.old

data class MoviesCatalogDto(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)