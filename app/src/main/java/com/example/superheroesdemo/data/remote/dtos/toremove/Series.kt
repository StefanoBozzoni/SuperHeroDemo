package com.example.superheroesdemo.data.remote.dtos.toremove

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)