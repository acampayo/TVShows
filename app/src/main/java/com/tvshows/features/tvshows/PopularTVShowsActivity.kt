package com.tvshows.features.tvshows

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tvshows.R
import com.tvshows.core.exception.Failure
import com.tvshows.core.platform.BaseActivity
import kotlinx.android.synthetic.main.activity_tvshows.*
import javax.inject.Inject

class PopularTVShowsActivity : BaseActivity() {

    @Inject lateinit var tvShowsAdapter: TVShowsAdapter

    lateinit var viewModel: PopularTVShowsViewModel

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
                    tvShowsList.post {
                        viewModel.loadPopularTvShows()
                        tvShowsAdapter.isLoading = true
                    }
                }
            }
        })

        tvShowsAdapter.clickListener = {
            val i = Intent(this, TVShowDetailActivity::class.java)
            i.putExtra(TVShow::class.simpleName, it)
            startActivity(i)
        }
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[PopularTVShowsViewModel::class.java]
        viewModel.tvShows.observe(this, Observer(::showPopularTvShows))
        viewModel.failure.observe(this, Observer(::handleFailure))
    }

    private fun loadPopularTvShows() {
        tvShowsList.visibility = View.GONE
        emptyView.visibility = View.GONE
        progress.visibility = View.VISIBLE
        viewModel.loadPopularTvShows()
    }

    private fun showPopularTvShows(tvShows: List<TVShow>?) {
        tvShowsList.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        tvShowsAdapter.tvShows = tvShows.orEmpty()
        progress.visibility = View.GONE
        tvShowsAdapter.isLoading = false
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> showFailure(getString(R.string.network_connection))
            is Failure.ServerError -> showFailure(getString(R.string.server_error))
            is Failure.APIError -> showFailure(failure.statusMessage)
        }
    }

    private fun showFailure(message: String) {
        tvShowsList.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        progress.visibility = View.GONE
        notify(message, R.string.action_reload, ::loadPopularTvShows)
    }
}