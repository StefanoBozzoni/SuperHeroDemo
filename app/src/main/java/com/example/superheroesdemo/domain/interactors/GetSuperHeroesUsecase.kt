package com.example.superheroesdemo.domain.interactors

import androidx.paging.PagingData
import androidx.paging.map
import com.example.superheroesdemo.data.repository.IRepository
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.domain.toDomain_SuperHero
import com.example.superheroesdemo.presentation.model.PreferencesType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSuperHeroesUsecase (
    private val remoteRepository: IRepository,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase
) {
    suspend fun execute(params: Params): Flow<PagingData<SuperHeroCharacter>> {

        with (params) {
            if (searchPreference == PreferencesType.Any) {
                return remoteRepository.getSuperHeroesCharacter(
                    searchNameText
                ).map { pagingData ->
                    pagingData.map {
                        var isLiked:Boolean? = null
                        if (retrieveIsLikedInfo) {
                            isLiked = getFavoriteStatusUseCase.execute(GetFavoriteStatusUseCase.Params(it.id)).getOrDefault(false)
                        }
                        it.toDomain_SuperHero(isLiked)
                    }
                }
            } else {
                //if searching for a like or dislike, looks in the db, favorites table
                return remoteRepository.getSuperHeroesCharacterFromDB(searchNameText, searchPreference.text).map { pagingData ->
                    pagingData.map { it.toDomain_SuperHero() }
                }
            }
        }
    }

    class Params(val searchNameText: String = "",
                 val searchPreference: PreferencesType = PreferencesType.Any,
                 val retrieveIsLikedInfo: Boolean = false
    )
}