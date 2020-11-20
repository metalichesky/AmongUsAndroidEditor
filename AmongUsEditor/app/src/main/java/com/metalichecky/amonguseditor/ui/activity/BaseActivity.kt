package com.metalichecky.amonguseditor.ui.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.metalichecky.amonguseditor.model.settings.Language
import com.metalichecky.amonguseditor.ui.MessageDialog
import com.metalichecky.amonguseditor.util.LocaleUtils
import com.metalichecky.amonguseditor.vm.SettingsViewModel
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {
    val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel.needSwitchLanguage.observe(this, Observer {
            if (it == true) {
                setLanguage(settingsViewModel.getCurrentLanguage())
                settingsViewModel.needSwitchLanguage.postValue(null)
            }
        })
    }

    fun showMessage(title: String?, message: String?, listener: MessageDialog.Listener? = null) {
        val fragmentManager = supportFragmentManager ?: return
        MessageDialog(title, message, listener).apply {
            this.show(fragmentManager)
        }
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleUtils.onAttachBaseContext(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            LocaleUtils.onApplyOverrideConfiguration(this, overrideConfiguration)
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    fun setLanguage(language: Language) {
        LocaleUtils.setLanguage(this, language.name)
        recreate()
    }

    fun setLanguage(language: String) {
        Timber.d("setLanguage() ${language}")
        LocaleUtils.setLanguage(this, language)
        recreate()
    }
}