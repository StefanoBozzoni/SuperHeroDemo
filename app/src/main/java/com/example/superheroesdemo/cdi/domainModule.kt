package com.example.superheroesdemo.cdi

import com.example.superheroesdemo.domain.interactors.GetSingleSuperHeroUseCase
import com.example.superheroesdemo.domain.interactors.GetSuperHeroesUsecase
import com.example.superheroesdemo.domain.interactors.UpdateFavoritesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetSingleSuperHeroUseCase(get()) }
    factory { UpdateFavoritesUseCase(get()) }
    factory { GetSuperHeroesUsecase(get()) }
}
