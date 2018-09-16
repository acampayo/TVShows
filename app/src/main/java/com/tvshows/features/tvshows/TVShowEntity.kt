package com.tvshows.features.tvshows

data class TVShowsEntity(val page: Int = 0,
                         val results: List<TVShow> = emptyList())