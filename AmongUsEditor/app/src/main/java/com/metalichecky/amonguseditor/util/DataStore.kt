package com.metalichecky.amonguseditor.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.metalichecky.amonguseditor.App
import com.metalichecky.amonguseditor.model.settings.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext


abstract class DataStore() : CoroutineScope {
    companion object {
        const val SETTINGS_DATA_SOURCE = "SETTINGS"
        val SETTINGS_LANGUAGE = preferencesKey<String>("SETTINGS_LANGUAGE")

    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    abstract class DataStoreObject(name: String, context: Context) {
        protected var dataStore: DataStore<Preferences> = context.createDataStore(
            name = name
        )
    }

    class Settings(
        context: Context = App.instance.applicationContext
    ) : DataStoreObject(
        SETTINGS_DATA_SOURCE,
        context
    ) {
        companion object {
            val languages = LanguageUtils.languages
            val defaultLanguage = LanguageUtils.detectDefaultLanguage()
        }

        val settingsFlow = dataStore.getFlow(SETTINGS_LANGUAGE)

        suspend fun setLanguage(language: Language) {
            dataStore.addToLocalStorage {
                this[SETTINGS_LANGUAGE] = language.locale.language
            }
        }

        suspend fun getLanguage(): Language {
            val code: String? = settingsFlow.first()
            return getLanguageFromCode(code)
        }

        private fun getLanguageFromCode(code: String?): Language {
            return languages.find {
                it.locale.language == code
            } ?: defaultLanguage
        }
    }
}

suspend inline fun <reified T> DataStore<Preferences>.getFromLocalStorage(preferencesKey: Preferences.Key<T>): T? {
    var value: T? = null
    data.catch {
        Timber.d("getFromLocalStorage() catch ${it}")
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        Timber.d("getFromLocalStorage() map ${it[preferencesKey]}")
        it[preferencesKey]
    }.collect {
        Timber.d("getFromLocalStorage() collect ${it}")
        value = it
    }
    Timber.d("getFromLocalStorage() result ${value}")
    return value
}

inline fun <reified T> DataStore<Preferences>.getFlow(preferencesKey: Preferences.Key<T>): Flow<T?> {
    return data.catch {
        Timber.d("getFromLocalStorage() catch ${it}")
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        Timber.d("getFromLocalStorage() map ${it[preferencesKey]}")
        it[preferencesKey]
    }
}

suspend fun DataStore<Preferences>.addToLocalStorage(mutableFunc: MutablePreferences.() -> Unit) {
    edit {
        mutableFunc(it)
    }
}