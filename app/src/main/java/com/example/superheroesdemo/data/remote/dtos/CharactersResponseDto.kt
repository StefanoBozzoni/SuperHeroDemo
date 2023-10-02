package com.example.superheroesdemo.data.remote.dtos

data class CharactersResponseDto(
    val code: Int,
    val status: String,
    val data: CharactersData?,
)