package com.metalichecky.amonguseditor.util

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsUtils {
    private const val DEFAULT_REQUEST_CODE = 21586
    val READ_WRITE_PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    const val READ_WRITE_PERMISSIONS_REQ_CODE = 11001
    val CAMERA_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )
    const val CAMERA_PERMISSIONS_REQ_CODE = 11002

    fun isPermissionsGranted(permissions: Map<String, Boolean>): Boolean {
        var granted = true
        for (permission in permissions) {
            granted = granted && permission.value
            if (!granted) break
        }
        return granted
    }

    fun isPermissionGranted(activity: Activity, permission: String): Boolean {
        val result = if (Build.VERSION.SDK_INT >= 23) {
            activity.checkSelfPermission(permission)
        } else {
            ActivityCompat.checkSelfPermission(activity, permission)
        }
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun isPermissionsGranted(activity: Activity, permissions: Array<String>): Boolean {
        var permissionsGranted = true
        for (permission in permissions) {
            permissionsGranted = permissionsGranted && isPermissionGranted(activity, permission)
            if (!permissionsGranted) {
                break
            }
        }
        PackageManager.PERMISSION_DENIED
        return permissionsGranted
    }

    fun isPermissionGranted(application: Application, permission: String): Boolean {
        val result = if (Build.VERSION.SDK_INT >= 23) {
            application.checkSelfPermission(permission)
        } else {
            ContextCompat.checkSelfPermission(application, permission)
        }
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun isPermissionsGranted(application: Application, permissions: Array<String>): Boolean {
        var permissionsGranted = true
        for (permission in permissions) {
            permissionsGranted = permissionsGranted && isPermissionGranted(application, permission)
            if (!permissionsGranted) {
                break
            }
        }
        PackageManager.PERMISSION_DENIED
        return permissionsGranted
    }

    fun filterNonGrantedPermissions(activity: Activity, permissions: Array<String>): List<String> {
        val nonGrantedPermissions = mutableListOf<String>()
        for (permission in permissions) {
            val result = if (Build.VERSION.SDK_INT >= 23) {
                activity.checkSelfPermission(permission)
            } else {
                ActivityCompat.checkSelfPermission(activity, permission)
            }
            if (result != PackageManager.PERMISSION_GRANTED) {
                nonGrantedPermissions.add(permission)
            }
        }
        return nonGrantedPermissions
    }

    fun shouldShowRequestPermissionRationale(
        activity: Activity,
        permissions: Array<String>
    ): Boolean {
        var shouldShow = false
        for (permission in permissions) {
            val result = if (Build.VERSION.SDK_INT >= 23) {
                activity.shouldShowRequestPermissionRationale(permission)
            } else {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            }
            shouldShow = shouldShow || result
            if (result) {
                break
            }
        }
        return shouldShow
    }

    fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int = DEFAULT_REQUEST_CODE
    ) {
        if (Build.VERSION.SDK_INT >= 23) {
            activity.requestPermissions(permissions, requestCode)
        } else {
            return
        }
    }

    fun requestPermissions(
        launcher: ActivityResultLauncher<Array<out String>>,
        permissions: Array<String>
    ) {
        if (Build.VERSION.SDK_INT >= 23) {
            launcher.launch(permissions)
        } else {
            return
        }
    }


    fun isExternalStorageManager(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            true
        }
    }
}