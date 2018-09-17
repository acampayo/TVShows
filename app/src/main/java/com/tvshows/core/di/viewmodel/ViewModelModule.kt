package com.tvshows.core.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tvshows.features.tvshows.PopularTVShowsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PopularTVShowsViewModel::class)
    internal abstract fun popularTVShowsViewModel(viewModel: PopularTVShowsViewModel): ViewModel

    //Add more ViewModels here
}