package com.example.superheroesdemo.domain

import com.example.superheroesdemo.data.local.model.FavoritesItem
import com.example.superheroesdemo.data.remote.dtos.CharactersResult
import com.example.superheroesdemo.domain.model.CharacterDetailInfo
import com.example.superheroesdemo.domain.model.ItemResourceInfo
import com.example.superheroesdemo.domain.model.SuperHeroCharacter

fun CharactersResult.toDomain(): CharacterDetailInfo =
     CharacterDetailInfo(
         toDomain_SuperHero(isLiked = false),
         series = series?.items?.map { ItemResourceInfo(it.name,it.resourceURI) }?: emptyList(),
         stories = stories?.items?.map { ItemResourceInfo(it.name,it.resourceURI) }?: emptyList(),
         events = events?.items?.map { ItemResourceInfo(it.name,it.resourceURI) }?: emptyList(),
         comics = comics?.items?.map { ItemResourceInfo(it.name,it.resourceURI) }?: emptyList(),
         links = urls?.map { ItemResourceInfo(it.type,it.url) }?: emptyList(),
     )


fun CharactersResult.toDomain_SuperHero(isLiked: Boolean? = null): SuperHeroCharacter =
        SuperHeroCharacter(
            id = id,
            description = description,
            name = name,
            resourceURI = resourceURI,
            thumbnailUrl = "${thumbnail.path}${if (thumbnail.extension.isBlank()) "" else "."}${thumbnail.extension}".replace("http://","https://"),
            isLiked = isLiked
        )

fun SuperHeroCharacter.toFavItemData(checked: Boolean): FavoritesItem =
    FavoritesItem(
        id = id,
        posterPath = thumbnailUrl,
        isLiked = if (checked) 1 else 0,
        characterName = name
    )


