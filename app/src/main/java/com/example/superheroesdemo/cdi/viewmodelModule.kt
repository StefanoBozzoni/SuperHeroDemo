package com.example.superheroesdemo.cdi

import com.example.superheroesdemo.presentation.viewmodels.CardLikesViewModel
import com.example.superheroesdemo.presentation.viewmodels.DetailViewModel
import com.example.superheroesdemo.presentation.viewmodels.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { CardLikesViewModel(get()) }
}

