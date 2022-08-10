package com.licorcafe.rickandmortyapp.character.characterList

import com.licorcafe.rickandmortyapp.character.domain.CharacterId
import com.licorcafe.rickandmortyapp.common.presentation.JetpackViewModel

sealed class CharacterAction
object Refresh : CharacterAction()
data class LoadDetails(val id: CharacterId) : CharacterAction()

sealed class CharactersEffect
data class NavigateToDetails(val characterId: CharacterId) : CharactersEffect()

typealias CharacterViewModel = JetpackViewModel<CharactersViewState, CharactersEffect>
