package com.tvshows.features.tvshows

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tvshows.R
import kotlinx.android.synthetic.main.activity_tvshow_detail.*
import kotlinx.android.synthetic.main.content_tvshow_detail.*

class TVShowDetailActivity : AppCompatActivity() {

    lateinit var tvShow: TVShow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tvshow_detail)
        tvShow = intent.getParcelableExtra(TVShow::class.simpleName)
        toolbar.title = tvShow.name
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        initializeViews()
    }

    private fun initializeViews() {
        overview.text = tvShow.overview

        Glide.with(this)
                .load(tvShow.absoluteBackdropUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(backdropImage)
    }
}
