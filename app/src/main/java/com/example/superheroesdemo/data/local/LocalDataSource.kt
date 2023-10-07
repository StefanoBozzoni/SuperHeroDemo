package com.example.superheroesdemo.data.local

import com.example.superheroesdemo.data.local.database.AppDatabase
import com.example.superheroesdemo.data.local.model.FavoritesItem

class LocalDataSource(private val database: AppDatabase) {

    suspend fun storeFavoriteItem(favoritesItem: FavoritesItem) {
        database.favoritesDao().insertFavorite(favoritesItem)
    }

    suspend fun getFavoriteItem(id: Int): Boolean? {
        val favoriteStatus = database.favoritesDao().getFavoriteCharacter(id)
        return when (favoriteStatus?.isLiked) {
                1 -> true
                0 -> false
                else -> null
        }
    }

    suspend fun getPagedFavoriteItemList(searchKey:String, isLiked:Int, startFrom: String, pageSize: Int ): List<FavoritesItem> =
            (database.favoritesDao().getFirstAlphabeticalFavorites(searchKey, isLiked, startFrom, pageSize))


    suspend fun getNextKey(searchKey: String, isLiked:Int, startFrom: String): String? =
            (database.favoritesDao().geNextAlphabeticKey(searchKey, isLiked, startFrom))


    suspend fun getPreviousKey(searchKey: String, isLiked:Int, startFrom: String): String? =
            (database.favoritesDao().getPreviousAlphabeticKey(searchKey, isLiked, startFrom))


}