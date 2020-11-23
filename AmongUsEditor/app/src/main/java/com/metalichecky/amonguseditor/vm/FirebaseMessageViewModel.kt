package com.metalichecky.amonguseditor.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.metalichecky.amonguseditor.model.FirebaseMessage
import com.metalichecky.amonguseditor.repo.DataStore
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FirebaseMessageViewModel @Inject constructor(): ViewModel() {
    private val firebaseMessages = DataStore.FirebaseMessages()
    val message = firebaseMessages.messageFlow.asLiveData()
    val needShowMessage = message.map {
        Timber.d("needShowMessage showInApp ${it.showInApp} wasShown ${it.wasShown} name ${it.name}")
        if (it.showInApp && !it.wasShown) {
            it
        } else {
            null
        }
    }

    init {

    }

    fun setMessageShown(message: FirebaseMessage) {
        viewModelScope.launch {
            message.wasShown = true
            firebaseMessages.setMessage(message)
        }
    }

}