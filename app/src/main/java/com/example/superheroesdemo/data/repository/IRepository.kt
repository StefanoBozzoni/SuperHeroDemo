package com.example.superheroesdemo.data.repository

import androidx.paging.PagingData
import com.example.superheroesdemo.data.local.model.FavoritesItem
import com.example.superheroesdemo.data.remote.dtos.CharactersResult
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getSingleCharacter(id: Int): Result<CharactersResult>
    suspend fun updateFavorite(item: FavoritesItem, checkFav: Boolean)
    suspend fun getFavoriteStatus(characterId: Int): Boolean?
    suspend fun getSuperHeroesCharacter(searchNameText: String): Flow<PagingData<CharactersResult>>
    suspend fun getSuperHeroesCharacterFromDB(searchNameText: String, searchPreference: String): Flow<PagingData<CharactersResult>>

}