package com.tvshows.features.tvshows

import android.arch.lifecycle.MutableLiveData
import com.tvshows.core.extension.addValues
import com.tvshows.core.platform.BaseViewModel
import javax.inject.Inject

class SimilarTVShowsViewModel
@Inject constructor(private val getSimilarTVShows: GetSimilarTVShows): BaseViewModel() {

    var page = 1
    var tvShows: MutableLiveData<MutableList<TVShow>> = MutableLiveData()

    fun loadSimilarTvShows(tvShowId: Int) {
        getSimilarTVShows(tvShowId, page) {
            it.either(::handleFailure, ::handleTVShows)
        }
    }

    private fun handleTVShows(tvShows: List<TVShow>) {
        when(this.tvShows.value == null) {
            true -> this.tvShows.value = tvShows.toMutableList()
            false -> this.tvShows.addValues(tvShows)
        }

        page++
    }
}