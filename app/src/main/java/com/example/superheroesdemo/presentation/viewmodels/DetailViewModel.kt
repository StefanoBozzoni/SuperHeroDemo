package com.example.superheroesdemo.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superheroesdemo.domain.interactors.GetSingleSuperHeroUseCase
import com.example.superheroesdemo.domain.model.CharacterDetailInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getSingleSuperHeroUseCase: GetSingleSuperHeroUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    val characterId = savedStateHandle.get<Int>("id")?:-1

    init {
        getSingleSuperHero(characterId)
    }

    private val _singleSuperHero = MutableStateFlow<Result<CharacterDetailInfo?>>(Result.success(null))
    val singleSuperHero : StateFlow<Result<CharacterDetailInfo?>>
        get() = _singleSuperHero

    fun getSingleSuperHero(id: Int) {
        viewModelScope.launch {
            val result = getSingleSuperHeroUseCase.execute(GetSingleSuperHeroUseCase.Params(id))
            _singleSuperHero.emit(result)
        }
    }

}