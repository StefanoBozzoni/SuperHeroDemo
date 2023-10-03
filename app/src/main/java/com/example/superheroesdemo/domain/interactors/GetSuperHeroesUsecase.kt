package com.example.superheroesdemo.domain.interactors

import androidx.paging.PagingData
import androidx.paging.map
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.data.repository.IRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSuperHeroesUsecase (private val remoteRepository: IRepository) {
    suspend fun execute(params: Params): Flow<PagingData<SuperHeroCharacter>> {
        return remoteRepository.getSuperHeroesCharacter(params.query).map { pagingData ->
            pagingData.map { it->
                SuperHeroCharacter(
                    id = it.id,
                    description = it.description,
                    name = it.name,
                    resourceURI = it.resourceURI,
                    thumbnailUrl = it.thumbnail.path+"."+it.thumbnail.extension
                )
            }
        }
    }
    class Params(val query: String = "")
}