package com.example.superheroesdemo.data.remote.dtos

data class CharactersResult(
    val id: Int,
    val description: String,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val thumbnail: CharactersThumbnail,
    //val series: Series,
    //val stories: Stories,
    //val events: Events,
    //val comics: Comics,
    //val urls: List<CaractersUrl>
)