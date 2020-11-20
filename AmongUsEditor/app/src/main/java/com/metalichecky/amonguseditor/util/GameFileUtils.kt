package com.metalichecky.amonguseditor.util

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.metalichecky.amonguseditor.App
import com.metalichecky.amonguseditor.BuildConfig
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path

object GameFileUtils {
    const val AMONG_US_FILES_DIR_NAME = "files"
    const val AMONG_US_PREFS_FILENAME = "playerPrefs"

    private var amongUsCachedAppDir: File? = null

    fun getAmongUsAppDir(): File? {
        if (amongUsCachedAppDir == null) {
            amongUsCachedAppDir = searchAmongUsAppDir()
        }
        return amongUsCachedAppDir
    }

    fun getAmongUsFilesDir(): File? {
        return File(getAmongUsAppDir(), AMONG_US_FILES_DIR_NAME)
    }

    fun isAmongUsAppExists(): Boolean {
        return getAmongUsAppDir()?.exists() ?: false
    }

    fun getAmongUsPrefsFile(): File? {
        return File(getAmongUsFilesDir(), AMONG_US_PREFS_FILENAME)
    }

    private fun getAmongUsPrefsSearchPath(): String {
        return Constants.AMONG_US_PACKAGE_NAME + File.separator +
                AMONG_US_FILES_DIR_NAME + File.separator +
                AMONG_US_PREFS_FILENAME
    }

    private fun searchAmongUsAppDir(): File? {
//        Timber.d("searchAmongUsPrefsFile() getExternalDir ${FileUtils.getExternalDir()}")
//        Timber.d("searchAmongUsPrefsFile() getExternalDir ${FileUtils.getExternalDir()?.list()?.joinToString { it }}")
//        Timber.d("searchAmongUsPrefsFile() getExternalSDCardDir ${FileUtils.getExternalSDCardDir()}")
//        Timber.d("searchAmongUsPrefsFile() getExternalSDCardDir ${FileUtils.getExternalSDCardDir()?.list()?.joinToString { it }}")

        var appDir: File? = FileUtils.getAppExternalDataDir(Constants.AMONG_US_PACKAGE_NAME)
        val defaultPathExists = appDir != null && appDir.exists()
        if (!defaultPathExists && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pathToSearch = getAmongUsPrefsSearchPath()
            val externalRoot = FileUtils.getExternalDir()
            var finded: File? = FileUtils.searchInDir(externalRoot?.toPath(), pathToSearch)?.toFile()
            if (finded == null) {
                val sdCardRoot = FileUtils.getExternalSDCardDir()
                finded = FileUtils.searchInDir(sdCardRoot?.toPath(), pathToSearch)?.toFile()
            }
            appDir = finded?.parentFile?.parentFile
        }
        return appDir
    }


}