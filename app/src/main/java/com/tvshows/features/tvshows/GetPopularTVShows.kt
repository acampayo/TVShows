package com.tvshows.features.tvshows

import com.tvshows.core.exception.Failure
import com.tvshows.core.exception.Failure.ServerError
import com.tvshows.core.functional.Either
import com.tvshows.core.functional.Either.Left
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetPopularTVShows
@Inject constructor(private val repository: TVShowsRepository) {

    operator fun invoke(page: Int, onResult: (Either<Failure, List<TVShow>>) -> Unit = {}) {
        repository.popularTvShows(page, onResult)
    }
}