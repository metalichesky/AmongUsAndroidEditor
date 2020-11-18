package com.metalichecky.amonguseditor.ui.fragment

import androidx.fragment.app.Fragment
import com.metalichecky.amonguseditor.ui.MessageDialog

abstract class BaseFragment: Fragment() {


    fun showMessage(title: String?, message: String?, listener: MessageDialog.Listener? = null) {
        val fragmentManager = activity?.supportFragmentManager ?: return
        MessageDialog(title, message, listener).apply {
            this.show(fragmentManager)
        }
    }

}