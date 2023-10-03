package com.example.superheroesdemo.domain.interactors

import com.example.superheroesdemo.data.repository.IRepository
import com.example.superheroesdemo.domain.model.CharacterDetailInfo
import com.example.superheroesdemo.domain.toDomain

class GetSingleSuperHeroUseCase(private val remoteRepository: IRepository) {
    suspend fun execute(params: Params): Result<CharacterDetailInfo> {

        val storedFavorite = try {
            remoteRepository.getFavoriteStatus(params.id)
        } catch (e:Exception) {
            return Result.failure(Exception("FAILED_RETRIEVE_FAVORITE"))
        }
        remoteRepository.getSingleCharacter(params.id).fold(
            onFailure = { return Result.failure(it) },
            onSuccess = {
                val result = it.toDomain()
                result.favorite = storedFavorite
                return Result.success(result)
            }
        )
    }

    class Params(val id: Int)
}