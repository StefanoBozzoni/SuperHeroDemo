package com.example.superheroesdemo.domain.model

data class SuperHeroCharacter(
    val id: Int,
    val description: String,
    val name: String,
    val resourceURI: String,
    val thumbnailUrl: String,
    var isLiked: Boolean?
)