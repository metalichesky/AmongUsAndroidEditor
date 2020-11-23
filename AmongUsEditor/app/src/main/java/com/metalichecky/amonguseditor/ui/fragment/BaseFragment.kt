package com.metalichecky.amonguseditor.ui.fragment

import androidx.fragment.app.Fragment
import com.metalichecky.amonguseditor.model.NavigateType
import com.metalichecky.amonguseditor.model.Screen
import com.metalichecky.amonguseditor.ui.MessageDialog
import com.metalichecky.amonguseditor.util.FirebaseLogger

abstract class BaseFragment: Fragment() {
    abstract val screen: Screen?

    override fun onStart() {
        super.onStart()
        screen?.let {
            FirebaseLogger.logNavigate(it, NavigateType.OPEN)
        }
        screen?.start()
    }

    override fun onStop() {
        super.onStop()
        screen?.let {
            FirebaseLogger.logNavigate(it, NavigateType.CLOSE)
        }
    }

    fun showMessage(title: String?, message: String?, listener: MessageDialog.Listener? = null) {
        val fragmentManager = activity?.supportFragmentManager ?: return
        MessageDialog(title, message, listener).apply {
            this.show(fragmentManager)
        }
    }
}