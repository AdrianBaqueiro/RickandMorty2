package com.licorcafe.rickandmortyapp.character.data

import com.licorcafe.rickandmortyapp.common.PaginatedEnvelope
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface RickAndMortyService {

    @GET("character")
    suspend fun getList(): PaginatedEnvelope<CharacterDto>

    @GET("character/?page={pageNumber}")
    suspend fun getListPage(@Path("characterId") characterId: Long): PaginatedEnvelope<CharacterDto>

    @GET("character/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: Long): CharacterDto

    @GET
    suspend fun getLocation(@Url locationUrl: String): LocationDetailsDto

}