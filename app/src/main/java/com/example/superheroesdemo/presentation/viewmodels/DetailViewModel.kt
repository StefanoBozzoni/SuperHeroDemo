package com.example.superheroesdemo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.superheroesdemo.domain.interactors.GetSingleSuperHeroUseCase
import com.example.superheroesdemo.domain.model.CharacterDetailInfo

class DetailViewModel(
    private val getSingleSuperHeroUseCase: GetSingleSuperHeroUseCase
): ViewModel() {

    suspend fun suspendGetSingleSuperHero(id: Int): CharacterDetailInfo {
        return getSingleSuperHeroUseCase.execute(GetSingleSuperHeroUseCase.Params(id)).getOrElse {
            throw it
        }
    }

}