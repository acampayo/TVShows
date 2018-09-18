package com.tvshows.core.di

import android.content.Context
import com.tvshows.AndroidApplication
import com.tvshows.BuildConfig
import com.tvshows.features.tvshows.TVShowsApi
import com.tvshows.features.tvshows.TVShowsRepository
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides @Singleton fun provideApplicationContext(): Context = application
    @Provides @Singleton fun thShowsRepositoryProvider(
            network: TVShowsRepository.Network): TVShowsRepository = network

    @Provides @Singleton fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(TVShowsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.
                        createWithScheduler(Schedulers.io()))
                .build()
    }

    private fun createHttpClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor {
            val original = it.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter(TVShowsApi.API_KEY_PARAM, BuildConfig.apiKey)
                    .build()

            val requestBuilder = original.newBuilder()
                    .url(url)

            val request = requestBuilder.build()
            it.proceed(request)
        }

        return okHttpClientBuilder.build()
    }

}
