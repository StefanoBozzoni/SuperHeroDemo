package com.example.superheroesdemo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.superheroesdemo.domain.interactors.GetSuperHeroesUsecase
import com.example.superheroesdemo.domain.interactors.UpdateFavoritesUseCase
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.presentation.model.RefreshEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardLikesViewModel(
    private val getSuperHeroUC: GetSuperHeroesUsecase,
    private val updateFavoritesUC: UpdateFavoritesUseCase,
): ViewModel() {

    private var _superHeroFlow = MutableStateFlow(PagingData.empty<SuperHeroCharacter>())
    val superHeroFlow: StateFlow<PagingData<SuperHeroCharacter>>
        get() = _superHeroFlow

    val showThumbUp = MutableStateFlow(RefreshEvent(false))

    init {
        getSHCharacters("", true)
    }

    fun resetFlow() {
        _superHeroFlow = MutableStateFlow(PagingData.empty())
    }

    fun getSHCharacters(searchNameText: String, retrieveIsLikedInfo: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            resetFlow()
            getSuperHeroUC.execute(
                    GetSuperHeroesUsecase.Params(searchNameText = searchNameText, retrieveIsLikedInfo = retrieveIsLikedInfo)
            ).cachedIn(this)
             .collect { _superHeroFlow.emit(it) }
        }
    }

    fun updateFavSuperHero(item: SuperHeroCharacter, favChecked: Boolean) {
        viewModelScope.launch {
            updateFavoritesUC.execute(UpdateFavoritesUseCase.Params(item, favChecked))
        }
    }

}