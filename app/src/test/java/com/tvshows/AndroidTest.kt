package com.tvshows

import android.app.Application
import android.content.Context
import com.tvshows.core.platform.BaseActivity
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
        application = AndroidTest.ApplicationStub::class,
        sdk = [21])
abstract class AndroidTest {

    @Rule
    @JvmField val injectMocks = TestRule { statement, _ ->
        MockitoAnnotations.initMocks(this@AndroidTest)
        statement
    }

    fun context(): Context = RuntimeEnvironment.application

    fun activityContext(): Context = Mockito.mock(BaseActivity::class.java)

    internal class ApplicationStub : Application()
}