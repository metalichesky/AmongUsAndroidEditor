package com.metalichecky.amonguseditor.util

import android.Manifest

object Constants {
    const val AMONG_US_PACKAGE_NAME = "com.innersloth.spacemafia"

    val neededPermissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )



}