package com.example.superheroesdemo.data.repository

import androidx.paging.PagingData
import com.example.superheroesdemo.data.local.model.FavoritesItem
import com.example.superheroesdemo.data.remote.dtos.old.Movie
import com.example.superheroesdemo.data.remote.dtos.old.ReviewsCatalog
import com.example.superheroesdemo.data.remote.dtos.old.VideoCatalog
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getSingleCharacter(id: Int): SuperHeroCharacter?
    suspend fun updateFavorite(item: FavoritesItem, checkFav: Boolean)
    suspend fun getFavoriteStatus(characterId: Int): Boolean
    suspend fun getSuperHeroesCharacter(query: String): Flow<PagingData<SuperHeroCharacter>>
}