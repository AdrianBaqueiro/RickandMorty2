package com.licorcafe.rickandmortyapp

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.licorcafe.rickandmortyapp.character.data.RickAndMortyService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

private val empty: (Any) -> Unit = { }

interface AppModule : RickAndMortyService {

    val afterBind: (Any) -> Unit
        get() = empty

    companion object {

        private val authInterceptor = { chain: Interceptor.Chain ->
            val ts = System.currentTimeMillis()

            val request = chain.request()
            val url = request.url
                .newBuilder()
                .addQueryParameter("ts", ts.toString())
                .build()
            val updated = request.newBuilder()
                .url(url)
                .build()
            chain.proceed(updated)
        }


        private val userAgentInterceptor = { chain: Interceptor.Chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("User-Agent", "Characters app/${BuildConfig.VERSION_CODE}")
                    .build()
            )
        }

        private val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(userAgentInterceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) })
                }
            }
            .build()

        private val contentType = "application/json".toMediaType()
        private val converter = Json {
            ignoreUnknownKeys = true
        }

        @OptIn(ExperimentalSerializationApi::class)
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(converter.asConverterFactory(contentType))
            .client(client)
            .build()

        private val rickAndMortyService = retrofit.create(RickAndMortyService::class.java)

        fun create(
            rickAndMortyService: RickAndMortyService = this.rickAndMortyService
        ): AppModule =
            object : AppModule, RickAndMortyService by rickAndMortyService {}
    }
}