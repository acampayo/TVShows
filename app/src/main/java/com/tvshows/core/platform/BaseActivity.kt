package com.tvshows.core.platform

import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.tvshows.AndroidApplication
import com.tvshows.R
import com.tvshows.core.di.ApplicationComponent
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as AndroidApplication).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    internal fun notify(@StringRes message: Int, @StringRes actionText: Int, action: () -> Any) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message,
                Snackbar.LENGTH_INDEFINITE)

        snackBar.setAction(actionText) { _ -> action.invoke() }
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        snackBar.show()
    }
}