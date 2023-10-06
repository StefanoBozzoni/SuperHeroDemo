package com.example.superheroesdemo.domain.interactors

import androidx.paging.PagingData
import androidx.paging.map
import com.example.superheroesdemo.data.repository.IRepository
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.domain.toDomain_SuperHero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSuperHeroesUsecase (private val remoteRepository: IRepository) {
    suspend fun execute(params: Params): Flow<PagingData<SuperHeroCharacter>> {

        if (params.searchLikeText == "Any" || params.searchLikeText.isEmpty()) {
            return remoteRepository.getSuperHeroesCharacter(params.searchNameText).map { pagingData ->
                pagingData.map { it.toDomain_SuperHero() }
            }
        } else {
            //if searching for a like or dislike, looks in the db, favorites table
            return remoteRepository.getSuperHeroesCharacterFromDB( params.searchNameText, params.searchLikeText).map { pagingData ->
                pagingData.map { it.toDomain_SuperHero() }
            }
        }
    }

    class Params(val searchNameText: String = "", val searchLikeText: String = "Any")
}