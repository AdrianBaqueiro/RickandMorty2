package com.licorcafe.rickandmortyapp.character.characterList

import com.licorcafe.rickandmortyapp.common.ErrorTextRes
import com.licorcafe.rickandmortyapp.common.TextRes
import okhttp3.HttpUrl

data class CharactersViewEntity(val id: Long, val name: String, val imageUrl: HttpUrl)

sealed class CharactersViewState

object Loading : CharactersViewState()

data class Content(val characters: List<CharactersViewEntity>) : CharactersViewState()

data class Problem(val stringId: TextRes) : CharactersViewState()

val Problem.isRecoverable: Boolean
    get() = stringId is ErrorTextRes
