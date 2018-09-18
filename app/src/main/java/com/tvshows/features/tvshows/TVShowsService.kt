package com.tvshows.features.tvshows

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TVShowsService
@Inject constructor(retrofit: Retrofit) : TVShowsApi {
    private val tvShowsApi by lazy { retrofit.create(TVShowsApi::class.java) }

    override fun popularTvShows(page: Int) = tvShowsApi.popularTvShows(page)
    override fun similarTvShows(tvShowId: Int, page: Int) =
            tvShowsApi.similarTvShows(tvShowId, page)
}