package com.example.superheroesdemo.domain.pagination

import com.example.superheroesdemo.data.local.model.FavoritesItem
import com.example.superheroesdemo.data.remote.dtos.CharactersResult
import com.example.superheroesdemo.data.remote.dtos.CharactersThumbnail

fun FavoritesItem.toCarachtersResult(): CharactersResult {
    return CharactersResult(
        id = this.id,
        description = "",
        modified = "",
        name = this.characterName,
        resourceURI = this.posterPath,
        thumbnail = CharactersThumbnail("",this.posterPath),
        series = null,
        stories = null,
        events = null,
        comics = null,
        urls = listOf(),
    )
}