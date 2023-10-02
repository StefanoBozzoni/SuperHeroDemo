package com.example.superheroesdemo.domain.model

import com.example.superheroesdemo.data.remote.dtos.old.Movie
import com.example.superheroesdemo.data.remote.dtos.old.Review
import com.example.superheroesdemo.data.remote.dtos.old.Video

class CharacterDetailInfo(
    val review: List<Review>?,
    val videos: List<Video>?,
    val character: SuperHeroCharacter?,
    var favorite: Boolean
)