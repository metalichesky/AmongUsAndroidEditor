package com.metalichecky.amonguseditor.util

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.metalichecky.amonguseditor.App
import com.metalichecky.amonguseditor.model.NavigateType
import com.metalichecky.amonguseditor.model.Screen
import com.metalichecky.amonguseditor.model.item.Item

object FirebaseLogger {
    private const val EVENT_NAVIGATE = "navigate"
    private const val EVENT_NAVIGATE_TYPE = "type"
    private const val EVENT_NAVIGATE_SCREEN_NAME = "screen_name"
    private const val EVENT_NAVIGATE_DURATION = "duration"

    private const val EVENT_FILE = "file"
    private const val EVENT_FILE_TYPE = "file_type"
    private const val EVENT_FILE_PATH = "file_path"
    private const val EVENT_FILE_EVENT_TYPE = "event_type"

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(App.instance)
    }

    fun logSelectItem(item: Item) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, item.id.toLong())
            param(FirebaseAnalytics.Param.ITEM_NAME, item.name)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, item.typeName)
        }
    }

    fun logNavigate(screen: Screen, navigateType: NavigateType) {
        firebaseAnalytics.logEvent(EVENT_NAVIGATE) {
            param(EVENT_NAVIGATE_SCREEN_NAME, screen.name)
            param(EVENT_NAVIGATE_TYPE, navigateType.typeName)
            param(EVENT_NAVIGATE_DURATION, screen.getSpentDurationSec())
        }
    }

    fun logFile(path: String, type: String, exists: Boolean) {
        firebaseAnalytics.logEvent(EVENT_FILE) {
            val fileType = if (type.isNotEmpty()) {
                type
            } else {
                "null"
            }
            param(EVENT_FILE_TYPE, fileType)
            val filePath = if (path.isNotEmpty()) {
                path
            } else {
                "null"
            }
            param(EVENT_FILE_PATH, filePath)
            val eventType = if (exists) {
                "exists"
            } else {
                "not exists"
            }
            param(EVENT_FILE_EVENT_TYPE, eventType)
        }
    }
}

