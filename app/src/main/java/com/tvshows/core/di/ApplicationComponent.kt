package com.tvshows.core.di

import com.tvshows.AndroidApplication
import com.tvshows.core.di.viewmodel.ViewModelModule
import com.tvshows.core.platform.BaseActivity
import com.tvshows.core.platform.BaseViewModel
import com.tvshows.features.tvshows.PopularTVShowsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)
    fun inject(popularTVShowsActivity: PopularTVShowsActivity)
}