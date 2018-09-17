package com.tvshows.features.tvshows

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface TVShowsApi {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "http://image.tmdb.org/t/p/w500"
        const val API_KEY = "cb87aff7d1a8dec7fe7ad2e7c183358f"
        private const val API_KEY_PARAM = "api_key"
        private const val PAGE_PARAM = "page"
        private const val TV_SHOW_ID_PARAM = "tv_id"
        private const val POPULAR_TV_SHOWS = "tv/popular"
        private const val SIMILAR_TV_SHOWS = "tv/{$TV_SHOW_ID_PARAM}/similar"
    }

    @GET(POPULAR_TV_SHOWS)
    fun popularTvShows(@Query(API_KEY_PARAM) apiKey: String,
                       @Query(PAGE_PARAM) page: Int): Call<TVShowsEntity>

    @GET(SIMILAR_TV_SHOWS)
    fun similarTvShows(@Path(TV_SHOW_ID_PARAM) tvShowId: Int, @Query(API_KEY_PARAM) apiKey: String,
                       @Query(PAGE_PARAM) page: Int): Call<TVShowsEntity>
}