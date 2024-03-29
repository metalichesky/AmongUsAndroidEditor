package com.metalichecky.amonguseditor.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.metalichecky.amonguseditor.App


object IntentUtils {


    fun openAmongUsGame() {
        App.instance.packageManager.getLaunchIntentForPackage(Constants.AMONG_US_PACKAGE_NAME)
            ?.let {
                App.instance.startActivity(it)
            }
    }

    fun amongUsGameExists(): Boolean {
        try {
            App.instance.packageManager.getPackageInfo(Constants.AMONG_US_PACKAGE_NAME, 0)
            return true
        } catch (ex: PackageManager.NameNotFoundException) {
            return false
        }
    }


    fun openAppPermissionSettings(context: Context) {
        context.startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", App.instance.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    fun isActivityExists(context: Context, intent: Intent): Boolean {
        try {
            return context.packageManager.resolveActivity(intent, 0) != null;
        } catch (ex: Exception) {
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun openManageAllFilesSettings(context: Context): Boolean {
        var intent = Intent().apply {
            action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
            return true
        } catch (ex: Exception) {
            // ignore
        }
        intent = Intent().apply {
            action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
            return true
        } catch (ex: Exception) {
            // ignore
        }
        return false
    }

}