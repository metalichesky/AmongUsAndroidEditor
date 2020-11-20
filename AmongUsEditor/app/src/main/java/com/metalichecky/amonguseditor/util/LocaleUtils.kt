package com.metalichecky.amonguseditor.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleUtils {
    private val DEFAULT_LANGUAGE = Locale.getDefault().language

    private val settings = DataStore().Settings()

    fun onAttachBaseContext(context: Context?): Context? {
        val lang = getLanguage()
        println("onAttachBaseContext() ${lang}")
        return setLanguage(context, lang)
    }

    fun onApplyOverrideConfiguration(context: Context?, config: Configuration?) {
        val configuration = config ?: return
        val locale = Locale(getLanguage())
        Locale.setDefault(locale)
        println("onApplyOverrideConfiguration() ${locale}")
        val resources = context?.resources ?: return
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            configuration.setLocale(locale)
            context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }

    fun getLanguage(): String {
        return settings.getLocal() ?: DEFAULT_LANGUAGE
    }

    fun setLanguage(context: Context?, language: String): Context? {
        settings.setLocal(language)
        return updateResources(context, language)
    }


    private fun updateResources(context: Context?, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context?.resources ?: return context
        val configuration = resources.configuration
        configuration.setLocale(locale)
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            configuration.setLocale(locale)
            context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
            context
        }
    }
}