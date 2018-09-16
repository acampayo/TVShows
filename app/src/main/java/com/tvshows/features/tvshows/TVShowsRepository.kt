package com.tvshows.features.tvshows

import com.fernandocejas.sample.core.exception.Failure
import com.fernandocejas.sample.core.exception.Failure.NetworkConnection
import com.fernandocejas.sample.core.functional.Either
import com.fernandocejas.sample.core.functional.Either.Left
import com.fernandocejas.sample.core.functional.Either.Right
import com.tvshows.core.functional.NetworkHandler
import retrofit2.Call
import javax.inject.Inject

interface  TVShowsRepository {

    fun popularTvShows(apiKey: String, page: Int): Either<Failure, List<TVShow>>
    fun similarTvShows(tvShowId: Int, apiKey: String, page: Int): Either<Failure, List<TVShow>>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val service: TVShowsService) : TVShowsRepository {

        override fun popularTvShows(apiKey: String, page: Int): Either<Failure, List<TVShow>> {
            return when (networkHandler.isConnected) {
                true -> request(service.popularTvShows(apiKey, page), { it.results }, TVShowsEntity())
                false, null -> Left(NetworkConnection())
            }
        }

        override fun similarTvShows(tvShowId: Int, apiKey: String, page: Int):
                Either<Failure, List<TVShow>> {
            return when (networkHandler.isConnected) {
                true -> request(service.similarTvShows(tvShowId, apiKey, page), { it.results }, TVShowsEntity())
                false, null -> Left(NetworkConnection())
            }
        }

        private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Right(transform((response.body() ?: default)))
                    false -> Left(Failure.ServerError())
                }
            } catch (exception: Throwable) {
                Left(Failure.ServerError())
            }
        }
    }
}