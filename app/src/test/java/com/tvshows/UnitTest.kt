package com.tvshows

import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class UnitTest {

    @Rule
    @JvmField val injectMocks = TestRule { statement, _ ->
        MockitoAnnotations.initMocks(this@UnitTest)
        statement
    }
}