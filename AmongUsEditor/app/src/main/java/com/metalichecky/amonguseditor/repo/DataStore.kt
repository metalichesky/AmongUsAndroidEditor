package com.metalichecky.amonguseditor.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.metalichecky.amonguseditor.App
import com.metalichecky.amonguseditor.model.FirebaseMessage
import com.metalichecky.amonguseditor.model.settings.Language
import com.metalichecky.amonguseditor.util.LanguageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.CoroutineContext


abstract class DataStore() : CoroutineScope {
    companion object {
        val SETTINGS_LANGUAGE = stringPreferencesKey("SETTINGS_LANGUAGE")
        val SETTINGS_GAME_APP_DIR = stringPreferencesKey("SETTINGS_GAME_APP_DIR")

        const val MESSAGES_DATA_SOURCE = "MESSAGES"
        const val MESSAGES_NAMES = "MESSAGES_NAMES"

        val MESSAGE_SHOW_IN_NOTIFICATION = booleanPreferencesKey("MESSAGE_SHOW_IN_NOTIFICATION")
        val MESSAGE_SHOW_IN_APP = booleanPreferencesKey("MESSAGE_SHOW_IN_APP")
        val MESSAGE_OPEN_APP_FROM_NOTIFICATION = booleanPreferencesKey("MESSAGE_SHOW_IN_APP")
        val MESSAGE_WAS_SHOWN = booleanPreferencesKey("MESSAGE_WAS_SHOWN")
        val MESSAGE_TITLE = stringPreferencesKey("MESSAGE_TITLE")
        val MESSAGE_TEXT = stringPreferencesKey("MESSAGE_TEXT")

    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    abstract class DataStoreObject() {
    }

    class Settings(
        context: Context = App.instance.applicationContext
    ) : DataStoreObject() {
        companion object {
            val languages =
                LanguageUtils.languages
            val defaultLanguage =
                LanguageUtils.detectDefaultLanguage()
        }
        protected val dataStore: DataStore<Preferences> = context.settingsDataStore

        val languageFlow = dataStore.getFlow(SETTINGS_LANGUAGE)
        val gameAppDirFlow = dataStore.getFlow(SETTINGS_GAME_APP_DIR)

        suspend fun setLanguage(language: Language) {
            dataStore.addToLocalStorage {
                this[SETTINGS_LANGUAGE] = language.locale.language
            }
        }

        suspend fun getLanguage(): Language {
            val code: String? = languageFlow.first()
            return getLanguageFromCode(code)
        }

        private fun getLanguageFromCode(code: String?): Language {
            return languages.find {
                it.locale.language == code
            } ?: defaultLanguage
        }

        suspend fun getGameAppDir(): String? {
            Timber.d("getGameAppDir() ${gameAppDirFlow.first()}")
            return gameAppDirFlow.first()
        }

        suspend fun setGameAppDir(gameAppDir: String) {
            dataStore.addToLocalStorage {
                Timber.d("setGameAppDir() ${gameAppDir}")
                this[SETTINGS_GAME_APP_DIR] = gameAppDir
            }
        }
    }


    class FirebaseMessages(
        context: Context = App.instance.applicationContext,
        messageName: String = FirebaseMessage.DEFAULT_NAME
    ) : DataStoreObject() {
        protected val dataStore: DataStore<Preferences> = context.messageDataStore
        val messageFlow = dataStore.createFlow().map {
            FirebaseMessage().apply {
                showInApp = it[MESSAGE_SHOW_IN_APP] ?: false
                showInNotification = it[MESSAGE_SHOW_IN_NOTIFICATION] ?: false
                openAppFromNotification = it[MESSAGE_OPEN_APP_FROM_NOTIFICATION] ?: false
                wasShown = it[MESSAGE_WAS_SHOWN] ?: false
                title = it[MESSAGE_TITLE] ?: ""
                text = it[MESSAGE_TEXT] ?: ""
            }
        }

        suspend fun getMessage(): FirebaseMessage {
            return messageFlow.first()
        }

        suspend fun setMessage(message: FirebaseMessage) {
            dataStore.addToLocalStorage {
                this[MESSAGE_SHOW_IN_APP] = message.showInApp
                this[MESSAGE_SHOW_IN_NOTIFICATION] = message.showInNotification
                this[MESSAGE_OPEN_APP_FROM_NOTIFICATION] = message.openAppFromNotification
                this[MESSAGE_WAS_SHOWN] = message.wasShown
                this[MESSAGE_TITLE] = message.title
                this[MESSAGE_TEXT] = message.text
            }
        }
    }
}

inline fun <reified T> DataStore<Preferences>.getFlow(preferencesKey: Preferences.Key<T>): Flow<T?> {
    return data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[preferencesKey]
    }
}

fun DataStore<Preferences>.createFlow(): Flow<Preferences> {
    return data.catch {
        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }
}

suspend fun DataStore<Preferences>.addToLocalStorage(mutableFunc: MutablePreferences.() -> Unit) {
    edit {
        mutableFunc(it)
    }
}

const val SETTINGS_DATA_SOURCE = "SETTINGS"
const val MESSAGE_DATA_SOURCE = "MESSAGE_"
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATA_SOURCE)
val Context.messageDataStore: DataStore<Preferences> by preferencesDataStore(name = MESSAGE_DATA_SOURCE)