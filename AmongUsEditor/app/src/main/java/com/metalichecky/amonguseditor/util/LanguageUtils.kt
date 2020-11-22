package com.metalichecky.amonguseditor.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.metalichecky.amonguseditor.model.settings.Language
import timber.log.Timber
import java.util.*

object LanguageUtils {
    val languages = listOf<Language>(
        Language.RUSSIAN,
        Language.ENGLISH
    )
    fun detectDefaultLanguage(): Language {
        val localeLanguageCode = Locale.getDefault().language
        return languages.find {
            it.locale.language == localeLanguageCode
        } ?: Language.ENGLISH
    }

    fun onAttachBaseContext(context: Context?, language: Language): Context? {
        val locale = language.locale
        println("onAttachBaseContext() currentLanguage ${locale}")
        println("onAttachBaseContext() ${locale}")
        return updateResources(context, locale)
    }

    fun onApplyOverrideConfiguration(context: Context?, config: Configuration?, language: Language) {
        println("onApplyOverrideConfiguration()")
        val configuration = config ?: return
        val locale = language.locale
        println("onApplyOverrideConfiguration() ${locale}")
        Locale.setDefault(locale)
        val resources = context?.resources ?: return
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            configuration.setLocale(locale)
            context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }

    fun setLanguage(context: Context?, language: Language): Context? {
        return updateResources(context, language.locale)
    }

    private fun updateResources(context: Context?, locale: Locale): Context? {
        Timber.d("updateResources() language ${locale.language}")
        Locale.setDefault(locale)
        val resources = context?.resources ?: return context
        val configuration = resources.configuration
        configuration.setLocale(locale)
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
            context
        }
    }
}