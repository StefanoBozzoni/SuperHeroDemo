package com.example.superheroesdemo.domain.interactors

import com.example.superheroesdemo.data.repository.IRepository
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.domain.toFavItemData

class UpdateFavoritesUseCase(private val repository: IRepository) {
    suspend fun execute(params: Params) {
        repository.updateFavorite(
            params.item.toFavItemData(params.checked),
            params.checked
        )
    }
    class Params(val item: SuperHeroCharacter, val checked: Boolean)
}