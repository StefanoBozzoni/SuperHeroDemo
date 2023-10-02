package com.example.superheroesdemo.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.superheroesdemo.domain.interactors.GetSuperHeroesUsecase
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getSuperHeroUC: GetSuperHeroesUsecase
): ViewModel() {

    private var _superHeroFlow = MutableStateFlow(PagingData.empty<SuperHeroCharacter>())
    val superHeroFlow: StateFlow<PagingData<SuperHeroCharacter>>
        get() = _superHeroFlow

    init {
        getSHCharacters("")
    }

    fun getSHCharacters(queryState: String) {
        _superHeroFlow =  MutableStateFlow(PagingData.empty())
        viewModelScope.launch {
            getSuperHeroUC.execute(GetSuperHeroesUsecase.Params(queryState))
                .cachedIn(this)
                .collect { _superHeroFlow.value = it }
        }
    }

}