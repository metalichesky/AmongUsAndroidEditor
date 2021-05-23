package com.metalichecky.amonguseditor.util

import com.metalichecky.amonguseditor.App

class CatchedGamePrefsException(
    message: String,
    throwable: Throwable
): Exception(message, throwable) {
    companion object {
        fun create(throwable: Throwable): CatchedGamePrefsException {
            val isExternalStorageManager = PermissionsUtils.isExternalStorageManager()
            val isFilePermissionsGranted = PermissionsUtils.isPermissionsGranted(App.instance, PermissionsUtils.READ_WRITE_PERMISSIONS)
            var customMessage = "isExternalStorageManager $isExternalStorageManager isFilePermissionsGranted $isFilePermissionsGranted"
            return CatchedGamePrefsException(customMessage, throwable)
        }
    }
}