package com.metalichecky.amonguseditor.vm

import androidx.lifecycle.*
import com.metalichecky.amonguseditor.model.settings.Language
import com.metalichecky.amonguseditor.util.DataStore
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SettingsViewModel @Inject constructor(): ViewModel() {
    companion object {
        var currentLanguage: Language = DataStore.Settings.defaultLanguage
    }
    var settings = DataStore.Settings()

    val languages = DataStore.Settings.languages
    val settingsUpdated = MutableLiveData<Boolean>()

    init {
        settings.settingsFlow.asLiveData().observeForever{
            updateSettings()
        }
    }

    fun setLanguage(newLanguage: Language) {
        viewModelScope.launch {
            settings.setLanguage(newLanguage)
        }
    }

    fun updateSettings(onUpdated: (() -> Unit)? = null) {
        Timber.d("updateSettings()")
        viewModelScope.launch {
            // update all settings here
            currentLanguage = settings.getLanguage()

            // send message that all settings has been updated
            onUpdated?.invoke()
            settingsUpdated.postValue(true)
        }
    }
}

