package com.example.superheroesdemo.data.remote.dtos

data class CharactersData(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<CharactersResult>,
    val total: Int
)