package com.metalichecky.amonguseditor.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.metalichecky.amonguseditor.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(), HasAndroidInjector {
    @Inject
    lateinit var injectedViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun getViewModelFactory() = injectedViewModelFactory

    companion object {
        const val EXTRA_MESSAGE_TITLE = "EXTRA_MESSAGE_TITLE"
        const val EXTRA_MESSAGE_TEXT = "EXTRA_MESSAGE_TEXT"
        const val EXTRA_SHOW_MESSAGE = "EXTRA_SHOW_MESSAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchMessage()
    }

    private fun fetchMessage() {
        val showMessage = intent.extras?.getString(EXTRA_SHOW_MESSAGE)?.toBoolean() ?: false
        Timber.d("fetchMessage() show = ${showMessage}")
        if (showMessage) {
            val messageText = intent.extras?.getString(EXTRA_MESSAGE_TEXT)
            val messageTitle = intent.extras?.getString(EXTRA_MESSAGE_TITLE)
            Timber.d("fetchMessage() title = ${messageTitle} text = ${messageText}")
            if (messageText != null || messageTitle != null) {
                showMessage(messageTitle, messageText)
            }
        }
    }
}