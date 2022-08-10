package com.licorcafe.rickandmortyapp.common

import kotlinx.serialization.Serializable

@Serializable
data class Paginated<A>(val results: List<A>)

typealias PaginatedEnvelope<A> = Paginated<A>