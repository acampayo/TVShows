package com.tvshows.features.tvshows

import com.tvshows.core.exception.Failure
import com.tvshows.core.exception.Failure.NetworkConnection
import com.tvshows.core.functional.Either
import com.tvshows.core.functional.Either.Left
import com.tvshows.core.functional.Either.Right
import com.tvshows.core.platform.NetworkHandler
import io.reactivex.Observable
import retrofit2.Call
import javax.inject.Inject

interface  TVShowsRepository {

    fun popularTvShows(page: Int): Either<Failure, List<TVShow>>
    fun similarTvShows(tvShowId: Int, page: Int): Either<Failure, List<TVShow>>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val service: TVShowsService) : TVShowsRepository {

        override fun popularTvShows(page: Int): Either<Failure, List<TVShow>> {
            return when (networkHandler.isConnected) {
                true -> request(service.popularTvShows(TVShowsApi.API_KEY, page), { it.results },
                        TVShowsEntity())
                false, null -> Left(NetworkConnection())
            }
        }

        override fun similarTvShows(tvShowId: Int, page: Int): Either<Failure, List<TVShow>> {
            return when (networkHandler.isConnected) {
                true -> request(service.similarTvShows(tvShowId, TVShowsApi.API_KEY, page),
                        { it.results }, TVShowsEntity())
                false, null -> Left(NetworkConnection())
            }
        }

        private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T):
                Either<Failure, R> {
            return try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> Right(transform((response.body() ?: default)))
                    false -> Left(Failure.ServerError())
                }
            } catch (exception: Throwable) {
                exception?.printStackTrace()
                Left(Failure.ServerError())
            }
        }
    }
}