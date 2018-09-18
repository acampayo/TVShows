package com.tvshows.features.tvshows

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tvshows.R
import kotlinx.android.synthetic.main.adapter_tvshow.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class TVShowsAdapter
@Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TV_SHOW = 0
    private val LOADING = 1

    internal var isLoading: Boolean by Delegates.observable(false) {
        _, _, _ -> notifyDataSetChanged()
    }

    internal var tvShows: List<TVShow> by Delegates.observable(emptyList()) {
        _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (TVShow) -> Unit = { _ -> }

    override fun getItemCount() = if(isLoading) tvShows.size + 1 else tvShows.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TV_SHOW -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_tvshow, parent, false))

            else -> LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_loading, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position < tvShows.size) {
            true -> TV_SHOW
            false -> LOADING
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as? ViewHolder)?.bind(tvShows[position], clickListener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TVShow, clickListener: (TVShow) -> Unit) = with(itemView) {
            setOnClickListener { clickListener(tvShow) }
            name.text = tvShow.name
            voteAverage.text = tvShow.voteAverage.toString()

            Glide.with(this.context.applicationContext)
                    .load(tvShow.absolutePosterUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(tvShowPoster)
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}