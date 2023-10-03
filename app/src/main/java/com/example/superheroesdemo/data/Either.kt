package com.example.superheroesdemo.data

class Either<R, T>(private val success: R? = null, private val failure: T? = null) {
    fun fold(successCond: ((R)->Unit), failureCond: ((T)->Unit)) {
        if (success != null) {
            successCond(success);
        }
        if (failure != null) {
            failureCond(failure);
        }
    }
}