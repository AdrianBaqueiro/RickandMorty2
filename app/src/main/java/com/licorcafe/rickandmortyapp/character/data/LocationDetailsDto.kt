package com.licorcafe.rickandmortyapp.character.data

import kotlinx.serialization.Serializable

@Serializable
data class LocationDetailsDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
)