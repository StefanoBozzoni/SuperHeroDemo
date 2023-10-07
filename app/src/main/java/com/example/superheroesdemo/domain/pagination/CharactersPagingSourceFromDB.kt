package com.example.superheroesdemo.domain.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.superheroesdemo.data.local.LocalDataSource
import com.example.superheroesdemo.data.remote.dtos.CharactersResult
import com.example.superheroesdemo.data.repository.Repository.Companion.PAGINATION_PAGE_SIZE
import com.example.superheroesdemo.domain.InvalidPreferenceSearchException

class CharactersPagingSourceFromDB(
    private val localDataSource: LocalDataSource,
    private val searchNameText: String,
    private val searchLikeText: String
): PagingSource<String, CharactersResult>() {
    val PAGE_SIZE = PAGINATION_PAGE_SIZE
    val STARTING_KEY = ""
    override fun getRefreshKey(state: PagingState<String, CharactersResult>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, CharactersResult> {
        val searchLikeDB: Int = when(searchLikeText) {
            "Likes" -> 1
            "Dislikes" -> 0
            else -> throw InvalidPreferenceSearchException()
        }

        return try {
            val currentKey = params.key ?: STARTING_KEY
            Log.d("XDEBUG", "current key num $currentKey")

            //get a page of PAGE_SIZE items from the DB starting from currentKey
            val superHeroesList = localDataSource.getPagedFavoriteItemList(
                currentKey,
                searchLikeDB,
                searchNameText,
                PAGE_SIZE
            ).map {
                it.toCarachtersResult()
            }

            LoadResult.Page(
                data = superHeroesList,
                prevKey = if (currentKey == STARTING_KEY) {
                    null
                } else {
                    localDataSource.getPreviousKey(currentKey, searchLikeDB, searchNameText)
                },
                nextKey = if (superHeroesList.size < PAGE_SIZE) {
                    null
                } else {
                    val lastKey = searchLikeDB.toString()+superHeroesList[superHeroesList.size - 1].name
                    localDataSource.getNextKey(lastKey, searchLikeDB, searchNameText)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}