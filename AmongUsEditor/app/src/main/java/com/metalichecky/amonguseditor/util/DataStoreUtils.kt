package com.metalichecky.amonguseditor.util

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.preference.PreferenceManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.metalichecky.amonguseditor.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

class DataStore(
    val context: Context = App.instance.applicationContext,
) : CoroutineScope {
    companion object {
        const val SETTINGS_DATA_SOURCE = "SETTINGS"
        val SETTINGS_LOCALE = preferencesKey<String>("SETTINGS_LOCALE")

    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    abstract inner class DataStoreObject(name: String) {
        protected var dataStore: DataStore<Preferences> = context.createDataStore(
            name = name
        )
    }

    inner class Settings() : DataStoreObject(SETTINGS_DATA_SOURCE) {
        fun setLocal(language: String) {
            launch {
                dataStore.addToLocalStorage {
                    this[SETTINGS_LOCALE] = language
                    println("setLocal() ${language}")
                }
            }
        }

        fun getLocal(): String? {
            var value: String? = null
            launch {
                dataStore.getFromLocalStorage(SETTINGS_LOCALE) {
                    value = this
                    println("getLocal() ${value}")
                }
            }
            Thread.sleep(1)
            return value
        }
    }
}

suspend inline fun <reified T> DataStore<Preferences>.getFromLocalStorage(
    PreferencesKey: Preferences.Key<T>, crossinline func: T.() -> Unit
) {
    data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[PreferencesKey]
    }.collect {
//        it?.let {
        func.invoke(it as T)
//        }
    }
}

suspend fun DataStore<Preferences>.addToLocalStorage(mutableFunc: MutablePreferences.() -> Unit) {
    edit {
        mutableFunc(it)
    }
}