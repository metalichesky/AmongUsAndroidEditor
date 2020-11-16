package com.metalichecky.amonguseditor.util

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.metalichecky.amonguseditor.App
import timber.log.Timber
import java.io.File
import java.nio.file.Files
import java.nio.file.Path


object FileUtils {

    fun getAppsExternalDataDir(): File? {
        val currentAppDir = App.instance.getExternalFilesDir(null)
        return currentAppDir?.parentFile?.parentFile
    }

    fun getAppExternalDataDir(appPackageName: String): File? {
        val appsExternalDataDir = getAppsExternalDataDir()
        Timber.d("getAppExternalDataDir() ${appsExternalDataDir?.absolutePath}")
        return File(appsExternalDataDir, appPackageName)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun searchInDir(dir: Path?, pathToSearch: String): Path? {
        var findedPath: Path? = null
        var cPath: String
        try {
            Files.newDirectoryStream(dir).use{ stream ->
                for (path in stream) {
                    // Don't want to continue iteration if we found it
                    if (!Files.isDirectory(path)) {
                        // Apparently not case sensitive, very useful
                        if (path.endsWith(pathToSearch)) {
                            cPath = path.toString()
                            return path
                        }
                    } else {
                        findedPath = searchInDir(path, pathToSearch)
                        if (findedPath != null) {
                            return findedPath
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return findedPath
    }

    fun getExternalDir(): File? {
        val systemVariables = listOf("EXTERNAL_STORAGE", "SECONDARY_STORAGE")
        var externalDir: File? = null
        for (variable in systemVariables) {
            externalDir = getSystemDirectory(variable)
            if (externalDir != null) {
                break
            }
        }
        return externalDir
    }

    fun getExternalSDCardDir(): File? {
        return SDCard.getSdCardPath(App.instance)
    }

    private fun getSystemDirectory(
        variableName: String,
        vararg paths: String
    ): File? {
        val path = System.getenv(variableName)
        if (path != null && !TextUtils.isEmpty(path)) {
            if (path.contains(":")) {
                for (_path in path.split(":".toRegex()).toTypedArray()) {
                    val file = File(_path)
                    if (file.exists()) {
                        return file
                    }
                }
            } else {
                val file = File(path)
                if (file.exists()) {
                    return file
                }
            }
        }
        if (paths != null && paths.isNotEmpty()) {
            for (_path in paths) {
                val file = File(_path)
                if (file.exists()) {
                    return file
                }
            }
        }
        return null
    }

}