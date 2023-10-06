package com.example.superheroesdemo.domain.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.superheroesdemo.data.local.LocalDataSource
import com.example.superheroesdemo.data.remote.dtos.CharactersResult
import com.example.superheroesdemo.data.remote.dtos.CharactersThumbnail

class CharactersPagingSourceFromDB(
    private val localDataSource: LocalDataSource,
    private val searchNameText: String,
    private val searchLikeText: String
): PagingSource<String, CharactersResult>() {
    val PAGE_SIZE = 20
    val STARTING_KEY = "0"
    override fun getRefreshKey(state: PagingState<String, CharactersResult>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, CharactersResult> {
        val searchLikeDB: Int = when(searchLikeText) {
            "Likes" -> 1
            "Dislikes" -> 0
            else -> throw Exception("Invalid DB search for preference")
        }

        return try {
            val currentKey = params.key ?: STARTING_KEY
            Log.d("XDEBUG", "current key num $currentKey")
            val superHeroesList = localDataSource.getPagedFavoriteItemList(searchLikeDB, searchNameText, PAGE_SIZE).map {
                CharactersResult(
                    id = it.id,
                    description = "",
                    modified = "",
                    name = it.characterName,
                    resourceURI = it.posterPath,
                    thumbnail = CharactersThumbnail("",it.posterPath),
                    series = null,
                    stories = null,
                    events = null,
                    comics = null,
                    urls = listOf(),
                )
            }

            LoadResult.Page(
                data = superHeroesList,
                prevKey = if (currentKey == STARTING_KEY) null else localDataSource.getPreviousKey(searchLikeDB, currentKey),
                nextKey = if (superHeroesList.size<PAGE_SIZE) null else localDataSource.getNextKey(searchLikeDB, currentKey),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}