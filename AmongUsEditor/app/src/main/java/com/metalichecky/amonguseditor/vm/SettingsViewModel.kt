package com.metalichecky.amonguseditor.vm

import androidx.lifecycle.*
import com.metalichecky.amonguseditor.model.settings.Language
import com.metalichecky.amonguseditor.repo.DataStore
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(): ViewModel() {
    companion object {
        var currentLanguage: Language = DataStore.Settings.defaultLanguage
    }
    var settings = DataStore.Settings()

    val languages = DataStore.Settings.languages
    val settingsUpdated = MutableLiveData<Boolean>()

    init {
        settings.languageFlow.asLiveData().observeForever{
            updateSettings(false)
        }
    }

    fun setLanguage(newLanguage: Language) {
        viewModelScope.launch {
            settings.setLanguage(newLanguage)
            updateSettings(true)
        }
    }

    fun updateSettings(notifyOnUpdated: Boolean = true, onUpdated: (() -> Unit)? = null) {
        viewModelScope.launch {
            // update all settings here
            currentLanguage = settings.getLanguage()

            onUpdated?.invoke()

            if (notifyOnUpdated) {
                // send message that all settings has been updated
                settingsUpdated.postValue(true)
            }
        }
    }
}

