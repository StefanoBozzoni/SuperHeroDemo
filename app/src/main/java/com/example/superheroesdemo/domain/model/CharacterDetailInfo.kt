package com.example.superheroesdemo.domain.model

import com.example.superheroesdemo.data.remote.dtos.CaractersUrl
import com.example.superheroesdemo.data.remote.dtos.ItemIdentification

class CharacterDetailInfo(
    val superHeroCharacter: SuperHeroCharacter,
    val series:  List<ItemIdentification>?,
    val stories: List<ItemIdentification>?,
    val events:  List<ItemIdentification>?,
    val comics:  List<ItemIdentification>?,
    val urls:    List<CaractersUrl>?,
    var favorite: Boolean
)