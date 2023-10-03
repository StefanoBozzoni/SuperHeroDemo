package com.example.superheroesdemo.data.local

import com.example.superheroesdemo.data.local.database.AppDatabase
import com.example.superheroesdemo.data.local.model.FavoritesItem

class LocalDataSource(private val database: AppDatabase) {

    suspend fun storeFavoriteItem(favoritesItem: FavoritesItem) {
        database.favoritesDao().insertFavorite(favoritesItem)
    }

    suspend fun deleteFavoriteItem(favoritesItem: FavoritesItem) {
        database.favoritesDao().deleteFavorite(favoritesItem)
    }

    suspend fun getFavoriteItemList(): List<FavoritesItem>? {
        return database.favoritesDao().getFavoriteCarachersList()
    }

    suspend fun getFavoriteItem(id: Int): Boolean {
        return (database.favoritesDao().getFavoriteCharacter(id) != null)
    }

}