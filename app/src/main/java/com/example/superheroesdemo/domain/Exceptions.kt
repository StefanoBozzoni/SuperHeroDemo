package com.example.superheroesdemo.domain

class FavoriteNotFoundException: Exception("Failed to retrieve favorite status")
class InvalidPreferenceSearchException: Exception("Invalid DB search for preference")