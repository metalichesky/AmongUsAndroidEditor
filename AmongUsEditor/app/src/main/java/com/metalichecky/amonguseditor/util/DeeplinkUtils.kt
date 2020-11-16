package com.metalichecky.amonguseditor.util

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.metalichecky.amonguseditor.App


object DeeplinkUtils {



    fun openAmongUsGame() {
        App.instance.packageManager.getLaunchIntentForPackage(Constants.AMONG_US_PACKAGE_NAME)?.let {
            App.instance.startActivity(it)
        }
    }

    fun openAppPermissionSettings() {
        App.instance.startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", App.instance.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }



}