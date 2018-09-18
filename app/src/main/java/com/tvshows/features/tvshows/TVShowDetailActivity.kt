package com.tvshows.features.tvshows

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tvshows.R
import com.tvshows.core.exception.Failure
import com.tvshows.core.platform.BaseActivity
import kotlinx.android.synthetic.main.activity_tvshow_detail.*
import kotlinx.android.synthetic.main.content_tvshow_detail.*
import javax.inject.Inject

class TVShowDetailActivity : BaseActivity() {

    @Inject lateinit var tvShowsAdapter: TVShowsAdapter

    private lateinit var tvShow: TVShow
    private lateinit var viewModel: SimilarTVShowsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvshow_detail)
        appComponent.inject(this)
        tvShow = intent.getParcelableExtra(TVShow::class.simpleName)
        toolbar.title = tvShow.name
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        initializeViewModel()
        initializeViews()
        loadSimilarTvShows()
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SimilarTVShowsViewModel::class.java]
        viewModel.tvShows.observe(this, Observer(::showSimilarTvShows))
        viewModel.failure.observe(this, Observer(::handleFailure))
    }

    private fun initializeViews() {
        overview.text = tvShow.overview

        Glide.with(this)
                .load(tvShow.absoluteBackdropUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(backdropImage)

        tvShowsList.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false)
        tvShowsList.adapter = tvShowsAdapter

        tvShowsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val layoutManager = tvShowsList.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + pastVisibleItems >= totalItemCount && !tvShowsAdapter.isLoading) {
                    viewModel.loadSimilarTvShows(tvShow.id)
                    tvShowsAdapter.isLoading = true
                }
            }
        })

        tvShowsAdapter.clickListener = {
            val i = Intent(this, TVShowDetailActivity::class.java)
            i.putExtra(TVShow::class.simpleName, it)
            startActivity(i)
        }
    }

    private fun loadSimilarTvShows() {
        progress.visibility = View.VISIBLE
        viewModel.loadSimilarTvShows(tvShow.id)
    }

    private fun showSimilarTvShows(tvShows: List<TVShow>?) {
        tvShowsAdapter.tvShows = tvShows.orEmpty()
        progress.visibility = View.GONE
        tvShowsAdapter.isLoading = false
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> showFailure(R.string.network_connection)
            is Failure.ServerError -> showFailure(R.string.server_error)
        }
    }
    private fun showFailure(@StringRes message: Int) {
        progress.visibility = View.GONE
        notify(message, R.string.action_reload, ::loadSimilarTvShows)
    }
}
