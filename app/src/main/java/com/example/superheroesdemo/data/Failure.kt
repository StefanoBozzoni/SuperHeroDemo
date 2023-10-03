package com.example.superheroesdemo.data

sealed class Failure
class DefaultFailure(val errorMessage: String?, e: Exception? = null): Failure()