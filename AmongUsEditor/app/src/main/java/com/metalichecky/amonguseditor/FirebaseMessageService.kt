package com.metalichecky.amonguseditor

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService
import com.metalichecky.amonguseditor.model.FirebaseMessage
import com.metalichecky.amonguseditor.repo.DataStore
import com.metalichecky.amonguseditor.util.NotificationsWorker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirebaseMessageService : FirebaseMessagingService() {
    companion object {
        const val TAG = "FirebaseMessageService"
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        GlobalScope.launch {
            val message = createMessage(remoteMessage)
            val dataStore = DataStore.FirebaseMessages(this@FirebaseMessageService)
            dataStore.setMessage(message)
            if (message.showInNotification) {
                showNotification(message)
            }
        }
    }

    private fun showNotification(message: FirebaseMessage) {
        val workerData = Data.Builder().apply {
            putString(NotificationsWorker.TITLE, message.title)
            putString(NotificationsWorker.TEXT, message.text)
            putString(NotificationsWorker.IMAGE_URL, message.imageUrl)
            putString(NotificationsWorker.LAUNCH_APP, message.openAppFromNotification.toString())
        }.build()
        val work = OneTimeWorkRequest.Builder(NotificationsWorker::class.java)
            .setInputData(workerData)
            .build()
        WorkManager.getInstance(this@FirebaseMessageService).beginWith(work).enqueue()
    }

    private fun createMessage(message: RemoteMessage?): FirebaseMessage {
        return if (message != null) FirebaseMessage(message) else FirebaseMessage()
    }
}
