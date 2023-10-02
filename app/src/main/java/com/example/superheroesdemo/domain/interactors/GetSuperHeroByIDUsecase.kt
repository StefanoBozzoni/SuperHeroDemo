package com.example.superheroesdemo.domain.interactors

import com.example.superheroesdemo.data.repository.IRepository
import com.example.superheroesdemo.domain.model.CharacterDetailInfo

class GetSuperHeroByIDUsecase(private val remoteRepository: IRepository) {
    suspend fun execute(params: Params): CharacterDetailInfo {
        val superHero   = remoteRepository.getSingleCharacter(params.id)
        //val videos  = remoteRepository.getCharacterComics(params.id)
        //val reviews = remoteRepository.getCharacterSeries(params.id)
        //val reviews = remoteRepository.getCharacterStories(params.id)
        //val reviews = remoteRepository.getCharacterEvents(params.id)
        val favorite = remoteRepository.getFavoriteStatus(params.id)
        return CharacterDetailInfo(
            null,
            null,
            superHero,
            favorite
        )
    }

    class Params(val id: Int)
}