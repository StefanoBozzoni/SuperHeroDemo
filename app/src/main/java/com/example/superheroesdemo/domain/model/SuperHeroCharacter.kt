package com.example.superheroesdemo.domain.model

import com.example.superheroesdemo.data.remote.dtos.CharactersThumbnail
data class SuperHeroCharacter(
    val id: Int,
    val description: String,
    val name: String,
    val resourceURI: String,
    val thumbnailUrl: String,
)