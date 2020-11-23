package com.metalichecky.amonguseditor.ui.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.metalichecky.amonguseditor.model.settings.Language
import com.metalichecky.amonguseditor.ui.MessageDialog
import com.metalichecky.amonguseditor.util.LanguageUtils
import com.metalichecky.amonguseditor.vm.SettingsViewModel
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity() {

    lateinit var settingsViewModel: SettingsViewModel

    abstract fun getViewModelFactory(): ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vmp = ViewModelProvider(this, getViewModelFactory())
        settingsViewModel = vmp.get(SettingsViewModel::class.java)
        settingsViewModel.settingsUpdated.observe(this, Observer {
            if (it != null) {
                updateSettings()
                settingsViewModel.settingsUpdated.postValue(null)
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
        val language = SettingsViewModel.currentLanguage
        val updatedContext = LanguageUtils.onAttachBaseContext(newBase, language)
        super.attachBaseContext(updatedContext)
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            val language = SettingsViewModel.currentLanguage
            LanguageUtils.onApplyOverrideConfiguration(
                this@BaseActivity,
                overrideConfiguration,
                language
            )
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun updateSettings() {
        LanguageUtils.setLanguage(this@BaseActivity, SettingsViewModel.currentLanguage)
        recreate()
    }
}