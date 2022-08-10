package com.licorcafe.rickandmortyapp.character.characterDetails

import com.licorcafe.rickandmortyapp.AppModule
import com.licorcafe.rickandmortyapp.R
import com.licorcafe.rickandmortyapp.character.CharacterException
import com.licorcafe.rickandmortyapp.character.NetworkError
import com.licorcafe.rickandmortyapp.character.ServerError
import com.licorcafe.rickandmortyapp.character.Unrecoverable
import com.licorcafe.rickandmortyapp.character.data.charactersDetails
import com.licorcafe.rickandmortyapp.character.domain.CharacterId
import com.licorcafe.rickandmortyapp.character.domain.LocationDetails
import com.licorcafe.rickandmortyapp.common.ErrorTextRes
import com.licorcafe.rickandmortyapp.common.IdTextRes
import com.licorcafe.rickandmortyapp.common.PlaceholderString
import com.licorcafe.rickandmortyapp.common.forkAndForget
import com.licorcafe.rickandmortyapp.common.parZip
import com.licorcafe.rickandmortyapp.common.presentation.ViewModelAlgebra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.HttpUrl.Companion.toHttpUrl
import com.licorcafe.rickandmortyapp.character.domain.Character
import com.licorcafe.rickandmortyapp.common.identity


interface CharacterDetailsModule : AppModule,
    ViewModelAlgebra<CharactersDetailsViewState, CharacterDetailsEffect>

suspend fun CharacterDetailsModule.program(
    characterId: CharacterId,
    actions: Flow<CharacterDetailsAction>
): Unit =
    parZip(Dispatchers.Default, { firstLoad(characterId) }, { handleActions(actions) })
    { _, _ -> }

suspend fun CharacterDetailsModule.handleActions(actions: Flow<CharacterDetailsAction>): Unit =
    actions.map { handleAction(it) }
        .forkAndForget(Dispatchers.Default, scope)

suspend fun CharacterDetailsModule.handleAction(action: CharacterDetailsAction) = when (action) {
    is Refresh -> loadCharacter(action.characterId)
    Up -> runEffect(NavigateUp)
}

suspend fun CharacterDetailsModule.firstLoad(characterId: CharacterId) {
    runInitialize { loadCharacter(characterId) }
}

suspend fun CharacterDetailsModule.loadCharacter(characterId: CharacterId) {
    setState(Loading)
    val viewState = runCatching { charactersDetails(characterId) }
        .map { (character, attribution) -> character.toViewEntity(attribution) to attribution }
        .map { (character, attribution) -> Content(character, attribution) }
        .fold(::identity, Throwable::toProblem)
    setState(viewState)
}

private fun Throwable.toProblem(): Problem = when (this) {
    is CharacterException -> when (error) {
        is NetworkError -> Problem(ErrorTextRes(R.string.error_recoverable_network))
        is ServerError -> Problem(ErrorTextRes(R.string.error_recoverable_server))
        is Unrecoverable -> Problem(IdTextRes(R.string.error_unrecoverable))
    }
    else -> Problem(IdTextRes(R.string.error_unrecoverable))
}

private fun Character.toViewEntity(attribution: LocationDetails): CharacterDetailsViewEntity =
    CharacterDetailsViewEntity(
        name = PlaceholderString(R.string.character_details_name, name),
        status = PlaceholderString(R.string.character_details_status, status),
        species = PlaceholderString(R.string.character_details_species, species),
        gender = PlaceholderString(R.string.character_details_gender, gender),
        origin = PlaceholderString(R.string.character_details_origin, origin.name),
        thumbnail = image.toHttpUrl(),
        locationName = PlaceholderString(R.string.character_details_location_details_name, attribution.name),
        locationType = PlaceholderString(R.string.character_details_location_details_type, attribution.type),
        locationDimension = PlaceholderString(R.string.character_details_location_details_dimension, attribution.dimension),
    )


