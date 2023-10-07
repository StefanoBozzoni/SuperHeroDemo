package com.example.superheroesdemo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.superheroesdemo.data.CharacterNotFoundException
import com.example.superheroesdemo.data.local.LocalDataSource
import com.example.superheroesdemo.data.local.RemoteDataSource
import com.example.superheroesdemo.data.local.model.FavoritesItem
import com.example.superheroesdemo.data.remote.dtos.CharactersResult
import com.example.superheroesdemo.domain.pagination.CharactersPagingSource
import com.example.superheroesdemo.domain.pagination.CharactersPagingSourceFromDB
import kotlinx.coroutines.flow.Flow

internal class Repository(private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource): IRepository {

    override suspend fun getSingleCharacter(id: Int): Result<CharactersResult> {
        val characterResult = try {
            remoteDataSource.appService.getSingleCharacter(id).body()?.data?.results
        } catch (e:Exception) {
            return Result.failure(e)
        }
        if (characterResult.isNullOrEmpty())
            return Result.failure(CharacterNotFoundException())
        else
            return Result.success(characterResult.first())
    }

    override suspend fun updateFavorite(item: FavoritesItem, checkFav: Boolean) {
        localDataSource.storeFavoriteItem(item)
    }
    override suspend fun getFavoriteStatus(characterId: Int): Boolean? {
        return localDataSource.getFavoriteItem(characterId)
    }

    override suspend fun getSuperHeroesCharacter(searchNameText: String): Flow<PagingData<CharactersResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGINATION_PAGE_SIZE,
                initialLoadSize = PAGINATION_INITIAL_SIZE, // Initial number of items loaded
                prefetchDistance = PAGINATION_PREFETCH_SIZE, // Number of items to prefetch
                enablePlaceholders = PAGINATION_ENABLE_PLACEHOLDERS  //returns loading items as placeholders
            ),
            pagingSourceFactory = {
                CharactersPagingSource(remoteDataSource, searchNameText)
            }
        ).flow
    }

    override suspend fun getSuperHeroesCharacterFromDB(searchNameText: String, searchPreference: String): Flow<PagingData<CharactersResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGINATION_PAGE_SIZE,
                initialLoadSize = PAGINATION_INITIAL_SIZE, // Initial number of items loaded
                prefetchDistance = PAGINATION_PREFETCH_SIZE, // Number of items to prefetch
                enablePlaceholders = PAGINATION_ENABLE_PLACEHOLDERS  //returns loading items as placeholders
            ),
            pagingSourceFactory = {
                CharactersPagingSourceFromDB(localDataSource, searchNameText, searchPreference)
            }
        ).flow
    }

    companion object {
        const val PAGINATION_PAGE_SIZE = 20
        const val PAGINATION_INITIAL_SIZE = 40
        const val PAGINATION_PREFETCH_SIZE = 10
        const val PAGINATION_ENABLE_PLACEHOLDERS = false
    }

}