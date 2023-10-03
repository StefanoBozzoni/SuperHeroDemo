package com.example.superheroesdemo.domain

import com.example.superheroesdemo.data.remote.dtos.CharactersResult
import com.example.superheroesdemo.domain.model.CharacterDetailInfo
import com.example.superheroesdemo.domain.model.SuperHeroCharacter

fun CharactersResult.toDomain(): CharacterDetailInfo {
     return CharacterDetailInfo(
         SuperHeroCharacter(
             id = id,
             description = description,
             name = name,
             resourceURI = resourceURI,
             thumbnailUrl = "${thumbnail.path}.${thumbnail.extension}"
         ),
         series = series?.items,
         stories = stories?.items,
         events = events?.items,
         comics = comics?.items,
         urls = urls,
         favorite = false,
     )
}
