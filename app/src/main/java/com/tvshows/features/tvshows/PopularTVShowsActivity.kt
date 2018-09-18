package com.tvshows.features.tvshows

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.tvshows.R
import com.tvshows.core.exception.Failure
import com.tvshows.core.platform.BaseActivity
import kotlinx.android.synthetic.main.activity_tvshows.*
import javax.inject.Inject

class PopularTVShowsActivity : BaseActivity() {

    @Inject lateinit var tvShowsAdapter: TVShowsAdapter

    private lateinit var viewModel: PopularTVShowsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvshows)
        appComponent.inject(this)
        initializeViews()
        initializeViewModel()
        loadPopularTvShows()
    }

    private fun initializeViews() {
        tvShowsList.layoutManager = GridLayoutManager(this, 2)
        tvShowsList.adapter = tvShowsAdapter

        tvShowsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val gridLayoutManager = tvShowsList.layoutManager as GridLayoutManager
                val visibleItemCount = gridLayoutManager.childCount
                val totalItemCount = gridLayoutManager.itemCount
                val pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + pastVisibleItems >= totalItemCount && !tvShowsAdapter.isLoading) {
                    viewModel.loadPopularTvShows()
                    tvShowsAdapter.isLoading = true
                }
            }
        })

        tvShowsAdapter.clickListener = {
            Log.d("testtt", it.name)
        }
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[PopularTVShowsViewModel::class.java]
        viewModel.tvShows.observe(this, Observer(::showPopularTvShows))
        viewModel.failure.observe(this, Observer(::handleFailure))
    }

    fun loadPopularTvShows() {
        tvShowsList.visibility = View.GONE
        emptyView.visibility = View.GONE
        progress.visibility = View.VISIBLE
        viewModel.loadPopularTvShows()
    }

    fun showPopularTvShows(tvShows: List<TVShow>?) {
        tvShowsList.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        tvShowsAdapter.tvShows = tvShows.orEmpty()
        progress.visibility = View.GONE
        tvShowsAdapter.isLoading = false
    }

    fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> showFailure(R.string.network_connection)
            is Failure.ServerError -> showFailure(R.string.server_error)
        }
    }

    fun showFailure(@StringRes message: Int) {
        tvShowsList.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        progress.visibility = View.GONE
        notify(message, R.string.action_reload, ::loadPopularTvShows)
    }
}