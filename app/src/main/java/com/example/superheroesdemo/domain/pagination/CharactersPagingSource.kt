package com.example.superheroesdemo.domain.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.superheroesdemo.data.remote.AppService
import com.example.superheroesdemo.domain.model.SuperHeroCharacter

class CharactersPagingSource(
    private val appService: AppService,
    private val query: String
): PagingSource<Int, SuperHeroCharacter>() {

    val STARTING_PAGE = 0
    override fun getRefreshKey(state: PagingState<Int, SuperHeroCharacter>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SuperHeroCharacter> {
        return try {
            val page = params.key ?: STARTING_PAGE
            Log.d("XDEBUG", "page num $page")
            val superHeroesList = appService.getCharacters(searchText = query.ifEmpty { null }, offset = page * 20, limit = 20).body()?.data?.results?.map {
                SuperHeroCharacter(
                    id = it.id,
                    description = it.description,
                    name = it.name,
                    resourceURI = it.resourceURI,
                    thumbnailUrl = it.thumbnail.path + "." + it.thumbnail.extension
                )
            } ?: emptyList()
            LoadResult.Page(
                data = superHeroesList,
                prevKey = if (page == STARTING_PAGE) null else page.minus(1),
                nextKey = if (superHeroesList.size<20) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}