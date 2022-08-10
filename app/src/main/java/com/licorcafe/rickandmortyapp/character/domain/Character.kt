package com.licorcafe.rickandmortyapp.character.domain

typealias CharacterId = Long

data class Location(val name: String, val url: String)

data class Character(
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Location,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
) {
    companion object {
        fun create(
            id: Long,
            name: String,
            status: String,
            species: String,
            type: String,
            gender: String,
            origin: Location,
            location: Location,
            image: String,
            episode: List<String>,
            url: String,
            created: String
        ): Character {
            return Character(
                id = id,
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender,
                origin = origin,
                location = location,
                image = image,
                episode = episode,
                url = url,
                created = created
            )
        }
    }
}

data class LocationDetails(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
) {
    companion object {
        fun create(
            id: Int,
            name: String,
            type: String,
            dimension: String,
            residents: List<String>,
            url: String,
            created: String
        ): LocationDetails {
            return LocationDetails(
                id = id,
                name = name,
                type = type,
                dimension = dimension,
                residents = residents,
                url = url,
                created = created
            )

        }
    }
}

data class Characters(val characters: List<Character>)
data class CharacterDetails(val characters: Character, val locationDetails: LocationDetails)