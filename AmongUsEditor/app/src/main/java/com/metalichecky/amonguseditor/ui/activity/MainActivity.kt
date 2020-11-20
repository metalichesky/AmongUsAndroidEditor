package com.metalichecky.amonguseditor.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.ui.MessageDialog
import timber.log.Timber

class MainActivity : AppCompatActivity() {
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
        val showMessage = intent.extras?.getBoolean(EXTRA_SHOW_MESSAGE, false) ?: false
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

    fun showMessage(title: String?, message: String?, listener: MessageDialog.Listener? = null) {
        val fragmentManager = supportFragmentManager ?: return
        MessageDialog(title, message, listener).apply {
            this.show(fragmentManager)
        }
    }
}