package com.metalichecky.amonguseditor.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metalichecky.amonguseditor.model.settings.Language
import com.metalichecky.amonguseditor.util.LocaleUtils
import timber.log.Timber

class SettingsViewModel: ViewModel() {
    private val languages = listOf<Language>(
        Language.RUSSIAN,
        Language.ENGLISH
    )
    val needSwitchLanguage = MutableLiveData<Boolean>()

    fun getLanguages(): List<Language> {
        return languages
    }

    fun getCurrentLanguage(): String {
        return getSelectedLanguage()?.name ?: LocaleUtils.getLanguage()
    }

    fun getSelectedLanguage(): Language? {
        return languages.find{
            it.selected
        }
    }

    fun setLanguage(language: Language) {
        var needSwitch = false
        languages.forEach {
            val selected = it.ordinal == language.ordinal
            if (!it.selected && selected) {
                needSwitch = true
            }
            it.selected = selected
        }
        needSwitchLanguage.postValue(needSwitch)
        Timber.d("setLanguage() ${language.name}")
    }



}

