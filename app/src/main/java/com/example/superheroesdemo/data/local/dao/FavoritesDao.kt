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

    @Query("SELECT * FROM ${TABLE_FAVORITES}")
    abstract suspend fun getFavoriteCarachersList(): List<FavoritesItem>?

    @Query("SELECT * FROM ${TABLE_FAVORITES} where id=:id")
    abstract suspend fun getFavoriteCharacter(id:Int): FavoritesItem?

    @Query("DELETE FROM ${TABLE_FAVORITES}")
    abstract suspend fun clearFavoritesTable()

    @Query("SELECT * FROM ${TABLE_FAVORITES} WHERE isLiked || characterName >= :isLiked || :searchKey AND isLiked = :isLiked "+
           "AND characterName LIKE :searchKey||'%' ORDER BY characterName ASC LIMIT :pageSize")
    abstract fun getFirstAlphabeticalFavorites(isLiked: Int, searchKey: String, pageSize: Int): List<FavoritesItem>

    @Query("SELECT isLiked || characterName FROM ${TABLE_FAVORITES} WHERE isLiked || characterName > :searchKey "+
           "AND isLiked = :isLiked AND characterName LIKE :searchKey||'%' ORDER BY characterName ASC LIMIT 1")
    abstract fun geNextAlphabeticKey(isLiked: Int, searchKey: String): String?

    @Query("SELECT isLiked || characterName FROM ${TABLE_FAVORITES} WHERE isLiked || characterName < :searchKey "+
           "AND isLiked = :isLiked AND characterName LIKE :searchKey||'%' ORDER BY characterName DESC LIMIT 1")
    abstract fun getPreviousAlphabeticKey(isLiked: Int, searchKey: String): String?
}