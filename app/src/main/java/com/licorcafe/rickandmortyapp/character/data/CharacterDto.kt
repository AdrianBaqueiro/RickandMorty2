package com.licorcafe.rickandmortyapp.character.data

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(val name: String, val url: String)

@Serializable
data class CharacterDto(
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationDto,
    val location: LocationDto,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)