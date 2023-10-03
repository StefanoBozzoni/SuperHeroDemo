package com.example.superheroesdemo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.superheroesdemo.data.local.LocalDataSource
import com.example.superheroesdemo.data.local.model.FavoritesItem
import com.example.superheroesdemo.data.remote.AppService
import com.example.superheroesdemo.data.remote.dtos.CharactersResult
import com.example.superheroesdemo.domain.pagination.CharactersPagingSource
import kotlinx.coroutines.flow.Flow

internal class Repository(private val appService: AppService, private val localDataSource: LocalDataSource): IRepository {

    override suspend fun getSingleCharacter(id: Int): Result<CharactersResult> {
        val characterResult = try {
            appService.getSingleCharacter(id).body()?.data?.results
        } catch (e:Exception) {
            return Result.failure(e)
        }
        if (characterResult.isNullOrEmpty())
            return Result.failure(Exception("NOT_FOUND"))
        else
            return Result.success(characterResult[0])
    }

    override suspend fun updateFavorite(item: FavoritesItem, checkFav: Boolean) {
        if (checkFav) {
            localDataSource.storeFavoriteItem(item)
        } else {
            localDataSource.deleteFavoriteItem(item)
        }
    }
    override suspend fun getFavoriteStatus(characterId: Int): Boolean {
        return localDataSource.getFavoriteItem(characterId)
    }

    override suspend fun getSuperHeroesCharacter(query: String): Flow<PagingData<CharactersResult>> {
        return Pager(
            config = PagingConfig(pageSize = 20,
                initialLoadSize = 40, // Initial number of items loaded
                prefetchDistance = 10, // Number of items to prefetch
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CharactersPagingSource(appService, query)
            }
        ).flow
    }
}