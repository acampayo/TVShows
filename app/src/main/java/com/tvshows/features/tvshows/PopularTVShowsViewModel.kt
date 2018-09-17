package com.tvshows.features.tvshows

import android.arch.lifecycle.MutableLiveData
import com.tvshows.core.platform.BaseViewModel
import javax.inject.Inject

class PopularTVShowsViewModel

@Inject constructor(private val getPopularTVShows: GetPopularTVShows): BaseViewModel() {

    var page = 0
    var tvShows: MutableLiveData<MutableList<TVShow>> = MutableLiveData()

    fun loadPopularTvShows() {
        page++
        getPopularTVShows(page) {
            it.either(::handleFailure, ::handleTVShows)
        }
    }

    private fun handleTVShows(tvShows: List<TVShow>) {
        if (this.tvShows.value == null) {
            this.tvShows.value = tvShows.toMutableList()
        } else {
            this.tvShows.value?.addAll(tvShows)
        }
    }
}