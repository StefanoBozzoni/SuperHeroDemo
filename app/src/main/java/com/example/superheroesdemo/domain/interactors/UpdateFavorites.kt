package com.example.superheroesdemo.domain.interactors

import com.example.superheroesdemo.data.local.model.FavoritesItem
import com.example.superheroesdemo.data.repository.IRepository

class UpdateFavorites(private val repository: IRepository) {
    suspend fun execute(params: Params) {
        repository.updateFavorite(params.item, params.checked)
    }
    class Params(val item: FavoritesItem, val checked: Boolean)
}