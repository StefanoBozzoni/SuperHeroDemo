package com.example.superheroesdemo.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.superheroesdemo.domain.interactors.GetSuperHeroesUsecase
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getSuperHeroUC: GetSuperHeroesUsecase
): ViewModel() {

    private var _superHeroFlow = MutableStateFlow(PagingData.empty<SuperHeroCharacter>())
    val superHeroFlow: StateFlow<PagingData<SuperHeroCharacter>>
        get() = _superHeroFlow

    init {
       getSHCharacters("","Any")
    }

    fun getSHCharacters(searchName: String, searchLike: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _superHeroFlow =  MutableStateFlow(PagingData.empty())
            getSuperHeroUC.execute(GetSuperHeroesUsecase.Params(searchName, searchLike))
                .cachedIn(this)
                .collect { _superHeroFlow.value = it }
        }
    }

}