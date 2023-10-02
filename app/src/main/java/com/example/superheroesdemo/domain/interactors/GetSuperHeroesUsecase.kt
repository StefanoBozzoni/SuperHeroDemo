package com.example.superheroesdemo.domain.interactors

import androidx.paging.PagingData
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.data.repository.IRepository
import kotlinx.coroutines.flow.Flow

class GetSuperHeroesUsecase (private val remoteRepository: IRepository) {
    suspend fun execute(params: Params): Flow<PagingData<SuperHeroCharacter>> {
        return remoteRepository.getSuperHeroesCharacter(params.query)
    }
    class Params(val query: String = "")
}