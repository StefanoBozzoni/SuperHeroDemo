package com.example.superheroesdemo.domain.model

class CharacterDetailInfo(
    val superHeroCharacter: SuperHeroCharacter,
    val series:  List<ItemResourceInfo>?,
    val stories: List<ItemResourceInfo>?,
    val events:  List<ItemResourceInfo>?,
    val comics:  List<ItemResourceInfo>?,
    val links:    List<ItemResourceInfo>?,
)