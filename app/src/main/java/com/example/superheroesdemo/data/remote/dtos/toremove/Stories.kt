package com.example.superheroesdemo.data.remote.dtos.toremove

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXXX>,
    val returned: Int
)