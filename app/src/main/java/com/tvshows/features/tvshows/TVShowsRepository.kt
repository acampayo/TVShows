package com.tvshows.features.tvshows

import com.tvshows.core.exception.Failure
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
                            when (it.isSuccessful) {
                                true -> onResult(Right(transform((it.body() ?: default))))
                                false -> onResult(Left(Failure.ServerError()))
                            }
                        } catch (exception: Throwable) {
                            exception.printStackTrace()
                            onResult(Left(Failure.ServerError()))
                        }
                    }, {
                        it.printStackTrace()
                        onResult(Left(Failure.ServerError()))
                    })
        }
    }
}