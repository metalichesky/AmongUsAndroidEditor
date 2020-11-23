package com.metalichecky.amonguseditor.model

import com.google.firebase.messaging.RemoteMessage

class FirebaseMessage () {
    companion object {
        const val KEY_NAME = "name"
        const val KEY_TITLE = "title"
        const val KEY_TEXT = "text"
        const val KEY_SHOW_IN_NOTIFICATION = "show_in_notification"
        const val KEY_SHOW_IN_APP = "show_in_app"
        const val KEY_OPEN_APP_FROM_NOTIFICATION = "open_app_from_notification"
        const val KEY_IMAGE_URL = "image_url"
        
        const val DEFAULT_NAME = "default"
        const val DEFAULT_TITLE = ""
        const val DEFAULT_TEXT = ""
        const val DEFAULT_SHOW_IN_NOTIFICATION = true
        const val DEFAULT_SHOW_IN_APP = false
        const val DEFAULT_OPEN_APP_FROM_NOTIFICATION = true
        const val DEFAULT_IMAGE_URL = ""
    }
    
    var name: String = DEFAULT_NAME
    var title: String = DEFAULT_TITLE
    var text: String = DEFAULT_TEXT
    var showInApp: Boolean = DEFAULT_SHOW_IN_APP
    var showInNotification: Boolean = DEFAULT_SHOW_IN_NOTIFICATION
    var openAppFromNotification: Boolean = DEFAULT_OPEN_APP_FROM_NOTIFICATION
    var imageUrl: String = DEFAULT_IMAGE_URL
    var wasShown: Boolean = false
    
    constructor(message: RemoteMessage): this() {
        name = message.data.get(KEY_NAME) ?: DEFAULT_NAME
        title = message.notification?.title ?: message.data.get(KEY_TITLE) ?: ""
        text = message.notification?.body ?: message.data.get(KEY_TEXT) ?: ""
        imageUrl = message.data.get(KEY_IMAGE_URL) ?: DEFAULT_IMAGE_URL
        showInApp = message.data.get(KEY_SHOW_IN_APP)?.toBoolean() ?: DEFAULT_SHOW_IN_APP
        showInNotification = message.data.get(KEY_SHOW_IN_NOTIFICATION)?.toBoolean() ?: DEFAULT_SHOW_IN_NOTIFICATION
        openAppFromNotification = message.data.get(KEY_OPEN_APP_FROM_NOTIFICATION)?.toBoolean() ?: DEFAULT_OPEN_APP_FROM_NOTIFICATION
    }
}
