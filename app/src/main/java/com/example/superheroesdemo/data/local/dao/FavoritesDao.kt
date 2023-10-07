package com.example.superheroesdemo.data.local.dao

import androidx.room.*
import com.example.superheroesdemo.data.local.database.DatabaseConstants.TABLE_FAVORITES
import com.example.superheroesdemo.data.local.model.FavoritesItem

@Dao
abstract class FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFavorite(cachedTest: FavoritesItem) : Long

    @Delete
    abstract suspend fun deleteFavorite(item: FavoritesItem) : Int

    @Query("SELECT count(*) FROM " + TABLE_FAVORITES)
    abstract suspend fun countTests(): Int

    @Query("SELECT * FROM ${TABLE_FAVORITES} where id=:id")
    abstract suspend fun getFavoriteCharacter(id:Int): FavoritesItem?

    @Query("DELETE FROM ${TABLE_FAVORITES}")
    abstract suspend fun clearFavoritesTable()

    //AND characterName LIKE :searchName||'%'
    @Query("SELECT * FROM ${TABLE_FAVORITES} WHERE (isLiked||characterName) >= :searchKey AND isLiked = :isLiked "+
           " AND characterName LIKE :searchName||'%' ORDER BY isLiked, characterName ASC LIMIT :pageSize")
    abstract suspend fun getFirstAlphabeticalFavorites(searchKey: String, isLiked: Int, searchName: String, pageSize: Int): List<FavoritesItem>

    @Query("SELECT isLiked||characterName FROM ${TABLE_FAVORITES} WHERE (isLiked || characterName) > :searchKey "+
           "AND isLiked = :isLiked AND characterName LIKE (:searchName||'%') ORDER BY isLiked, characterName ASC LIMIT 1")
    abstract suspend fun geNextAlphabeticKey(searchKey: String, isLiked: Int, searchName: String): String?

    @Query("SELECT isLiked || characterName FROM ${TABLE_FAVORITES} WHERE isLiked || characterName < :searchKey "+
           "AND isLiked = :isLiked AND characterName LIKE :searchName||'%' ORDER BY isLiked, characterName DESC LIMIT 1")
    abstract suspend fun getPreviousAlphabeticKey(searchKey: String, isLiked: Int, searchName: String): String?
}