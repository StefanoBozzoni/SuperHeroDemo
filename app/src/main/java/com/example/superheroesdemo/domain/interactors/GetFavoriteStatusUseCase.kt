package com.example.superheroesdemo.domain.interactors

import com.example.superheroesdemo.data.repository.IRepository
import com.example.superheroesdemo.domain.FavoriteNotFoundException

class GetFavoriteStatusUseCase(private val remoteRepository: IRepository) {
    suspend fun execute(params: Params): Result<Boolean?> =
        try {
            val favStatus = remoteRepository.getFavoriteStatus(params.id)
            Result.success(favStatus)
        } catch (e:Exception) {
            Result.failure(FavoriteNotFoundException())
        }

    class Params(val id: Int)
}