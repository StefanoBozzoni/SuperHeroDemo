package com.example.superheroesdemo.domain.interactors

import com.example.superheroesdemo.data.repository.IRepository
import com.example.superheroesdemo.domain.model.CharacterDetailInfo
import com.example.superheroesdemo.domain.toDomain

class GetSingleSuperHeroUseCase(
    private val remoteRepository: IRepository,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase
) {
    suspend fun execute(params: Params): Result<CharacterDetailInfo> {

        val isLiked = getFavoriteStatusUseCase.execute(GetFavoriteStatusUseCase.Params(params.id)).getOrElse {
            return Result.failure(it)
        }

        remoteRepository.getSingleCharacter(params.id).fold(
            onFailure = { return Result.failure(it) },
            onSuccess = {
                val character = it.toDomain()
                character.superHeroCharacter.isLiked = isLiked
                return Result.success(character)
            }
        )
    }

    class Params(val id: Int)
}