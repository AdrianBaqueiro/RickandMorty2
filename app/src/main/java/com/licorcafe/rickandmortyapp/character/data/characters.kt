package com.licorcafe.rickandmortyapp.character.data

import com.licorcafe.rickandmortyapp.character.CharacterException
import com.licorcafe.rickandmortyapp.character.NetworkError
import com.licorcafe.rickandmortyapp.character.ServerError
import com.licorcafe.rickandmortyapp.character.Unrecoverable
import com.licorcafe.rickandmortyapp.character.domain.CharacterDetails
import com.licorcafe.rickandmortyapp.character.domain.CharacterId
import com.licorcafe.rickandmortyapp.character.domain.Characters
import com.licorcafe.rickandmortyapp.character.domain.Location
import com.licorcafe.rickandmortyapp.character.domain.LocationDetails
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import com.licorcafe.rickandmortyapp.character.domain.Character

suspend fun RickAndMortyService.characters(): Characters {
    val charactersDto = runRefineError { getList() }
    val characters = charactersDto.results.map { it.toDomain() }
    return Characters(characters)
}

suspend fun RickAndMortyService.charactersDetails(characterId: CharacterId): CharacterDetails {
    val characterDto = runRefineError { getCharacter(characterId) }
    val character = characterDto.toDomain()
    val locationDetailsDto = runRefineError { getLocation(character.location.url) }
    val locationDetails = locationDetailsDto.toDomain()
    return CharacterDetails(character, locationDetails)
}

private fun LocationDetailsDto.toDomain(): LocationDetails = LocationDetails.create(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    residents = residents,
    url = url,
    created = created
)

private fun CharacterDto.toDomain(): Character = Character.create(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = Location(origin.name, origin.url),
    location = Location(location.name, location.url),
    image = image,
    episode = episode,
    url = url,
    created = created
)

private suspend fun <A> runRefineError(f: suspend () -> A): A =
    try {
        f()
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        throw when (e.code()) {
            in 500..599 -> CharacterException(
                ServerError(e.code(), e.message())
            )
            else -> CharacterException(Unrecoverable(e))
        }
    } catch (e: IOException) {
        throw CharacterException(NetworkError(e))
    } catch (e: Throwable) {
        throw CharacterException(Unrecoverable(e))
    }