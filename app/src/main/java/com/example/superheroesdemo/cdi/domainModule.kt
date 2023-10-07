package com.example.superheroesdemo.cdi

import com.example.superheroesdemo.domain.interactors.GetSingleSuperHeroUseCase
import com.example.superheroesdemo.domain.interactors.GetSuperHeroesUsecase
import com.example.superheroesdemo.domain.interactors.UpdateFavoritesUseCase
import com.example.superheroesdemo.domain.interactors.GetFavoriteStatusUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetSingleSuperHeroUseCase(get(),get()) }
    factory { UpdateFavoritesUseCase(get()) }
    factory { GetSuperHeroesUsecase(get(), get()) }
    factory { GetFavoriteStatusUseCase(get()) }
}
