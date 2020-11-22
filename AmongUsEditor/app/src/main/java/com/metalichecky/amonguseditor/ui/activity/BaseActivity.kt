package com.metalichecky.amonguseditor.ui.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.metalichecky.amonguseditor.model.settings.Language
import com.metalichecky.amonguseditor.ui.MessageDialog
import com.metalichecky.amonguseditor.util.DataStore
import com.metalichecky.amonguseditor.util.LanguageUtils
import com.metalichecky.amonguseditor.vm.SettingsViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

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
        Timber.d("updateSettings()")
        setLanguage(SettingsViewModel.currentLanguage)
    }

    private fun setLanguage(language: Language) {
        Timber.d("setLanguage() ${language.locale.language}")
        LanguageUtils.setLanguage(this@BaseActivity, language)
        recreate()
    }
}