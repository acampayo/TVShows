package com.tvshows.features.tvshows

import com.tvshows.AndroidTest
import com.tvshows.core.functional.Either
import com.tvshows.features.tvshows.GetPopularTVShows
import com.tvshows.features.tvshows.PopularTVShowsViewModel
import com.tvshows.features.tvshows.TVShow
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import kotlin.test.assertEquals

class SimilarTVShowsViewModelTest: AndroidTest() {

    private lateinit var similarTVShowsViewModel: SimilarTVShowsViewModel

    @Mock
    private lateinit var getSimilarTVShows: GetSimilarTVShows

    @Before
    fun setUp() {
        similarTVShowsViewModel = SimilarTVShowsViewModel(getSimilarTVShows)
    }

    @Test
    fun loadSimilarTVShows() {
        val tvShowsList = listOf(
                TVShow(1396,
                        "/1yeVJox3rjo2jBKrrihIMj7uoS9.jpg",
                        "/eSzpy96DwBujGFj0xMbXBcGcfxX.jpg",
                        8.1,
                        "",
                        "Breaking Bad"),
                TVShow(1399,
                        "/jIhL6mlT7AblhbHJgEoiBIOUVl1.jpg",
                        "/mUkuc2wyV9dHLG0D0Loaw5pO2s8.jpg",
                        7.91,
                        "",
                        "Game of Thrones")
        )

        Mockito.`when`(getSimilarTVShows(1, 1396)).thenAnswer { Either.Right(tvShowsList) }

        similarTVShowsViewModel.tvShows.observeForever {
            assertEquals(it!!.size, 1)

            assertEquals(it[0].id, 1396)
            assertEquals(it[0].posterPath, "/1yeVJox3rjo2jBKrrihIMj7uoS9.jpg")
            assertEquals(it[0].backdropPath, "/eSzpy96DwBujGFj0xMbXBcGcfxX.jpg")
            assertEquals(it[0].voteAverage, 8.1)
            assertEquals(it[0].overview, "")
            assertEquals(it[0].name, "Breaking Bad")

            assertEquals(it[1].id, 1399)
            assertEquals(it[1].posterPath, "/jIhL6mlT7AblhbHJgEoiBIOUVl1.jpg")
            assertEquals(it[1].backdropPath, "/mUkuc2wyV9dHLG0D0Loaw5pO2s8.jpg")
            assertEquals(it[1].voteAverage, 7.91)
            assertEquals(it[1].overview, "")
            assertEquals(it[1].name, "Game of Thrones")
        }

        similarTVShowsViewModel.loadSimilarTvShows(1396)
    }
}