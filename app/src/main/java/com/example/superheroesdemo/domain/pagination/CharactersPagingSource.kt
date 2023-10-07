package com.example.superheroesdemo.domain.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.superheroesdemo.data.local.RemoteDataSource
import com.example.superheroesdemo.data.remote.dtos.CharactersResult
import com.example.superheroesdemo.data.repository.Repository.Companion.PAGINATION_PAGE_SIZE

class CharactersPagingSource(
    private val remoteDataSource: RemoteDataSource,
    private val query: String,
) : PagingSource<Int, CharactersResult>() {

    val STARTING_PAGE = 0
    override fun getRefreshKey(state: PagingState<Int, CharactersResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharactersResult> {
        val PAGE_SIZE = PAGINATION_PAGE_SIZE
        return try {
            val page = params.key ?: STARTING_PAGE
            Log.d("XDEBUG", "page num $page")
            val superHeroesList = remoteDataSource
                .appService.getCharacters(
                    searchText = query.ifEmpty { null },
                    offset = page * PAGE_SIZE,
                    limit = PAGE_SIZE
                ).body()?.data?.results.orEmpty()

            LoadResult.Page(
                data = superHeroesList,
                prevKey = if (page == STARTING_PAGE) null else page.minus(1),
                nextKey = if (superHeroesList.size < PAGE_SIZE) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}