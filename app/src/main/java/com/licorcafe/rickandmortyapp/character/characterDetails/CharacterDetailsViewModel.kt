package com.licorcafe.rickandmortyapp.character.characterDetails

import com.licorcafe.rickandmortyapp.character.domain.CharacterId
import com.licorcafe.rickandmortyapp.common.presentation.JetpackViewModel


sealed class CharacterDetailsAction
data class Refresh(val characterId: CharacterId) : CharacterDetailsAction()
object Up : CharacterDetailsAction()

sealed class CharacterDetailsEffect
object NavigateUp : CharacterDetailsEffect()

typealias CharacterDetailsViewModel = JetpackViewModel<CharactersDetailsViewState, CharacterDetailsEffect>