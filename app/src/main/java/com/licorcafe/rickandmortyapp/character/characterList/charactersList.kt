package com.licorcafe.rickandmortyapp.character.characterList

import com.licorcafe.rickandmortyapp.AppModule
import com.licorcafe.rickandmortyapp.R
import com.licorcafe.rickandmortyapp.character.CharacterException
import com.licorcafe.rickandmortyapp.character.NetworkError
import com.licorcafe.rickandmortyapp.character.ServerError
import com.licorcafe.rickandmortyapp.character.Unrecoverable
import com.licorcafe.rickandmortyapp.character.data.characters
import com.licorcafe.rickandmortyapp.common.ErrorTextRes
import com.licorcafe.rickandmortyapp.common.IdTextRes
import com.licorcafe.rickandmortyapp.common.forkAndForget
import com.licorcafe.rickandmortyapp.common.parZip
import com.licorcafe.rickandmortyapp.common.presentation.ViewModelAlgebra
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.HttpUrl.Companion.toHttpUrl

interface CharactersModule : AppModule, ViewModelAlgebra<CharactersViewState, CharactersEffect>

suspend fun CharactersModule.program(actions: Flow<CharacterAction>): Unit =
    parZip(Dispatchers.Default, { firstLoad() }, { handleActions(actions) })
    { _, _ -> }

suspend fun CharactersModule.handleActions(actions: Flow<CharacterAction>) =
    actions.map { handleAction(it) }
        .forkAndForget(Dispatchers.Default, scope)

suspend fun CharactersModule.handleAction(action: CharacterAction) = when (action) {
    is LoadDetails -> runEffect(NavigateToDetails(action.id))
    Refresh -> loadCharacterOne()
}

suspend fun CharactersModule.firstLoad(): Unit =
        runInitialize { loadCharacterOne() }

suspend fun CharactersModule.loadCharacterOne() {
    setState(Loading)
    val state = try {
        val (characters) = characters()
        val charactersVE = characters.map { CharactersViewEntity(it.id, it.name, it.image.toHttpUrl()) }
        Content(charactersVE)
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        mapError(t)
    }
    setState(state)
}

private fun mapError(t: Throwable) = when (t) {
    is CharacterException -> when (t.error) {
        is NetworkError -> Problem(ErrorTextRes(R.string.error_recoverable_network))
        is ServerError -> Problem(ErrorTextRes(R.string.error_recoverable_server))
        is Unrecoverable -> Problem(IdTextRes(R.string.error_unrecoverable))
    }
    else -> Problem(IdTextRes(R.string.error_unrecoverable))
}