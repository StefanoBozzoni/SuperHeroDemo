package com.example.superheroesdemo.data.remote.dtos

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemIdentification>,
    val returned: Int
)