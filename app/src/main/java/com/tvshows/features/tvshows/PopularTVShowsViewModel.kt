package com.tvshows.features.tvshows

import android.arch.lifecycle.MutableLiveData
import com.tvshows.core.platform.BaseViewModel
import javax.inject.Inject

class PopularTVShowsViewModel
@Inject constructor(private val getPopularTVShows: GetPopularTVShows): BaseViewModel() {

    var page = 0
    var tvShows: MutableLiveData<List<TVShow>> = MutableLiveData()

    fun loadPopularTvShows() {
        page++
        getPopularTVShows(page)
    }

    private fun handleTVShows(tvShows: List<TVShow>) {
        this.tvShows.value = tvShows
    }
}