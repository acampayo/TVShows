package com.tvshows.features.tvshows

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.SmallTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.tvshows.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@SmallTest
@RunWith(AndroidJUnit4::class)
class PopularTVShowsActivityTest {

    private val position = 0

    @Rule @JvmField
    var mActivityTestRule = ActivityTestRule(PopularTVShowsActivity::class.java)

    @Test
    fun testRecyclerView() {
        mActivityTestRule.activity.viewModel.tvShows.observeForever {
            // First scroll to the position that needs to be matched and click on it.
            onView(ViewMatchers.withId(R.id.tvShowsList))
                    .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))

            // Match the text in an item below the fold and check that it's displayed.
            val itemElementText = it!![position].overview
            onView(withText(itemElementText)).check(matches(isDisplayed()))
        }
    }
}