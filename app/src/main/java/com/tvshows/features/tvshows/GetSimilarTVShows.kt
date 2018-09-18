package com.tvshows.features.tvshows

import com.tvshows.core.exception.Failure
import com.tvshows.core.functional.Either
import javax.inject.Inject

class GetSimilarTVShows
@Inject constructor(private val repository: TVShowsRepository) {

    operator fun invoke(page: Int, tvShowId: Int, onResult: (Either<Failure,
            List<TVShow>>) -> Unit = {}) {
        repository.similarTvShows(page, tvShowId, onResult)
    }
}