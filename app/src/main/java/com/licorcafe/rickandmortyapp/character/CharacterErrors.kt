package com.licorcafe.rickandmortyapp.character

sealed class CharacterError

sealed class Recoverable : CharacterError()
data class NetworkError(val t: Throwable) : Recoverable()
data class ServerError(val code: Int, val msg: String?) : Recoverable()

data class Unrecoverable(val t: Throwable) : CharacterError()

data class CharacterException(val error: CharacterError) : Throwable() {

    override val cause: Throwable?
        get() = when (error) {
            is NetworkError -> error.t
            is ServerError -> null
            is Unrecoverable -> error.t
        }

    override val message: String?
        get() = when (error) {
            is NetworkError -> "Network Error"
            is ServerError -> "Looks like the Marvel backend has issues"
            is Unrecoverable -> "Logic bug!"
        }

    override fun fillInStackTrace(): Throwable = this
}