package com.tvshows.features.tvshows

import com.google.gson.Gson
import com.tvshows.core.exception.Failure
import com.tvshows.core.exception.Failure.APIError
import com.tvshows.core.exception.Failure.NetworkConnection
import com.tvshows.core.functional.Either
import com.tvshows.core.functional.Either.Left
import com.tvshows.core.functional.Either.Right
import com.tvshows.core.platform.NetworkHandler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject


interface  TVShowsRepository {

    fun popularTvShows(page: Int, onResult: (Either<Failure, List<TVShow>>) -> Unit)
    fun similarTvShows(tvShowId: Int, page: Int, onResult: (Either<Failure, List<TVShow>>) -> Unit)

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val service: TVShowsService) : TVShowsRepository {

        override fun popularTvShows(page: Int, onResult: (Either<Failure, List<TVShow>>) -> Unit) {
            return when (networkHandler.isConnected) {
                true -> request(service.popularTvShows(page), { it.results }, TVShowsEntity(),
                        onResult)
                false, null -> onResult(Left(NetworkConnection()))
            }
        }

        override fun similarTvShows(tvShowId: Int, page: Int, onResult: (Either<Failure,
                List<TVShow>>) -> Unit) {
            return when (networkHandler.isConnected) {
                true -> request(service.similarTvShows(tvShowId, page), { it.results },
                        TVShowsEntity(), onResult)
                false, null -> onResult(Left(NetworkConnection()))
            }
        }

        private fun <T, R> request(call: Observable<Response<T>>, transform: (T) -> R, default: T,
                                   onResult: (Either<Failure, R>) -> Unit = {}) {
            call.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        try {
                            val result = when (it.isSuccessful) {
                                true -> Right(transform(it.body() ?: default))
                                false -> Left(handleApiError(it))
                            }
                            onResult(result)
                        } catch (exception: Throwable) {
                            exception.printStackTrace()
                            onResult(Left(Failure.ServerError()))
                        }
                    }, {
                        it.printStackTrace()
                        onResult(Left(Failure.ServerError()))
                    })
        }

        private fun<T> handleApiError(response: Response<T>): Failure {
            return when(response.code()) {
                400, 401 -> Gson().fromJson(response.errorBody()?.string(), APIError::class.java)
                else -> Failure.ServerError()
            }
        }
    }
}