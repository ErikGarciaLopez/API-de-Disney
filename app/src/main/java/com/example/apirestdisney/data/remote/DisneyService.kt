package com.example.apirestdisney.data.remote

import com.example.apirestdisney.data.remote.model.Character
import com.example.apirestdisney.data.remote.model.CharacterDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DisneyService {

    //https://api.disneyapi.dev/character?pageSize=7438
    @GET("character")
    fun getCharacterList(
        @Query("pageSize") pageSize: Int
    ): Call<Character>

    //https://api.disneyapi.dev/character/571
    @GET("character/{id}")
    fun getCharacterDetail(
        @Path("id") id: Int
    ): Call<CharacterDetail>

}