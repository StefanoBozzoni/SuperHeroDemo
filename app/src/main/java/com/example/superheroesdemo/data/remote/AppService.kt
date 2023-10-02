package com.example.superheroesdemo.data.remote

import com.example.superheroesdemo.data.remote.dtos.CharactersResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {

    @GET("/v1/public/characters")
    suspend fun getCharacters(@Query("nameStartsWith") searchText: String? = null, @Query("offset") offset: Int, @Query("limit") limit: Int): Response<CharactersResponseDto>

    @GET("/v1/public/characters/{id}")
    suspend fun getSingleCharacter(@Path("id") id: Int): Response<CharactersResponseDto>

}