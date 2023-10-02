package com.example.superheroesdemo.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(viewModelInstance: MainViewModel = koinViewModel(), characterId: Int, onNavBack: () -> Unit) {

    //TODO
}