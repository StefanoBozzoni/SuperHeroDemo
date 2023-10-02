package com.example.superheroesdemo.cdi

import com.example.superheroesdemo.domain.interactors.GetSuperHeroByIDUsecase
import com.example.superheroesdemo.domain.interactors.GetSuperHeroesUsecase
import com.example.superheroesdemo.domain.interactors.UpdateFavorites
import org.koin.dsl.module

val domainModule = module {
    factory { GetSuperHeroByIDUsecase(get()) }
    factory { UpdateFavorites(get()) }
    factory { GetSuperHeroesUsecase(get()) }
}
