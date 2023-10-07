package com.example.superheroesdemo.presentation.model

enum class PreferencesType(val text:String) {
    Any("Any"),
    Likes("Likes"),
    Dislikes("Dislikes");

    companion object {
        fun fromText(prefText: String): PreferencesType {
            return valueOf(prefText)
        }
    }
}