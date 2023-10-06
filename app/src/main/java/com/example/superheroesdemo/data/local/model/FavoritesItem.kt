package com.example.superheroesdemo.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.superheroesdemo.data.local.database.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TABLE_FAVORITES,
    indices = [Index(value = ["isLiked", "characterName"])]
)
data class FavoritesItem (
    @PrimaryKey
    val id: Int,
    val posterPath: String,
    val isLiked: Int,
    val characterName: String
)
