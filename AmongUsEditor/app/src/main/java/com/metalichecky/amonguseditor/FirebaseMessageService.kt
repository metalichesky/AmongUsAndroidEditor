package com.metalichecky.amonguseditor

import android.annotation.SuppressLint
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService
import com.metalichecky.amonguseditor.util.NotificationsWorker

class FirebaseMessageService : FirebaseMessagingService() {
    companion object {
        const val TAG = "FirebaseMessageService"


        const val MESSAGE_KEY_PICTURE_URL = "picture_url"
        const val MESSAGE_KEY_LAUNCH_APP = "launch_app"
        const val MESSAGE_KEY_SHOW_IN_APP = "show_message_in_app"
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        launchNotificationsWorker(remoteMessage)
    }

    @SuppressLint("LogNotTimber")
    private fun launchNotificationsWorker(remoteMessage: RemoteMessage?) {
        val notification = remoteMessage?.notification
//        Log.d("MessageReceiver","launchNotificationsWorker() title = ${notification?.title}" +
//                "text = ${notification?.body}" +
//                "imageUrl = ${remoteMessage?.data?.get("picture_url")}")
        val workerData = Data.Builder()
            .putString(NotificationsWorker.TITLE, notification?.title)
            .putString(NotificationsWorker.TEXT, notification?.body)
            .putString(NotificationsWorker.IMAGE_URL, remoteMessage?.data?.get(MESSAGE_KEY_PICTURE_URL))
            .putString(NotificationsWorker.LAUNCH_APP, remoteMessage?.data?.get(MESSAGE_KEY_LAUNCH_APP))
            .putString(NotificationsWorker.SHOW_IN_APP, remoteMessage?.data?.get(MESSAGE_KEY_SHOW_IN_APP))
            .build()
        Log.d(TAG, "messageReceived, print all values")
        remoteMessage?.data?.forEach {
            Log.d(TAG, "message key ${it.key} value ${it.value}")
        }
        val work = OneTimeWorkRequest.Builder(NotificationsWorker::class.java)
            .setInputData(workerData)
            .build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
    }

}
