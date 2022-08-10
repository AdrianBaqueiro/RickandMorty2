package com.licorcafe.rickandmortyapp.character.characterDetails

import com.licorcafe.rickandmortyapp.character.domain.LocationDetails
import com.licorcafe.rickandmortyapp.common.PlaceholderString
import com.licorcafe.rickandmortyapp.common.TextRes
import okhttp3.HttpUrl

data class CharacterDetailsViewEntity(
    val name: PlaceholderString,
    val status: PlaceholderString,
    val species: PlaceholderString,
    val gender: PlaceholderString,
    val origin: PlaceholderString,
    val thumbnail: HttpUrl,
    val locationName: PlaceholderString,
    val locationType: PlaceholderString,
    val locationDimension: PlaceholderString
)

sealed class CharactersDetailsViewState {
    val title: String
        get() = if (this is Content) character.name.replacement else ""
}

object Loading : CharactersDetailsViewState()

data class Content(
    val character: CharacterDetailsViewEntity,
    val attribution: LocationDetails
) : CharactersDetailsViewState()

data class Problem(val stringId: TextRes) : CharactersDetailsViewState()